package net.dancier.kikeriki.kafka;

import io.micrometer.core.instrument.util.IOUtils;
import net.dancier.kikeriki.messages.*;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

import static net.dancier.kikeriki.kafka.KikerikiConsumerTest.NUM_PARTITIONS;
import static net.dancier.kikeriki.kafka.KikerikiConsumerTest.TOPIC;
import static org.awaitility.Awaitility.await;


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "dancier.kikeriki.topic=" + TOPIC,
        "dancier.kikeriki.involve-dancer-after=1s",
        "dancier.kikeriki.involvement-check-interval=10s",
        "dancier.kikeriki.reinvolvement-interval=60s" })
@EmbeddedKafka(topics = TOPIC, partitions = NUM_PARTITIONS)
public class KikerikiConsumerTest
{
  public static final String TOPIC = "FOO";
  public static final int NUM_PARTITIONS = 2;
  public static final String TYPE_ID_HEADER = "__TypeId__";

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private TestMessageHandler messageHandler;

  @Value("classpath:messages/chat.json")
  Resource chatMessage;
  @Value("classpath:messages/chat-with-unknown-field.json")
  Resource chatMessageWithUnknownField;
  @Value("classpath:messages/login.json")
  Resource loginMessage;
  @Value("classpath:messages/login-with-unknown-field.json")
  Resource loginMessageWithUnknownField;
  @Value("classpath:messages/mail-sent.json")
  Resource mailSentMessage;
  @Value("classpath:messages/mail-sent-with-unknown-field.json")
  Resource mailSentMessageWithUnknownField;


  @Test
  @DirtiesContext
  public void testValidMessages()
  {
    await("Wait for service Kikeriki")
      .atMost(Duration.ofSeconds(5))
      .untilAsserted(() ->
        restTemplate.getForObject(
            "http://localhost:" + port + "/actuator/health",
            String.class
          )
          .contains("UP"));

    ProducerRecord<String, String> record;
    // The header <code>__TypeId__</code> is set by the JsonSerializer
    // on the sending side, if configured correctly

    record = new ProducerRecord<>(TOPIC, 0,"peter", read(loginMessage));
    record.headers().add(TYPE_ID_HEADER, Message.Type.LOGIN.name().getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, 1,"klaus", read(loginMessageWithUnknownField));
    record.headers().add(TYPE_ID_HEADER, Message.Type.LOGIN.name().getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, 0, "peter", read(chatMessage));
    record.headers().add(TYPE_ID_HEADER, Message.Type.CHAT.name().getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, 1, "klaus", read(chatMessageWithUnknownField));
    record.headers().add(TYPE_ID_HEADER, Message.Type.CHAT.name().getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, 0, "peter", read(mailSentMessage));
    record.headers().add(TYPE_ID_HEADER, Message.Type.MAIL_SENT.name().getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, 1, "klaus", read(mailSentMessageWithUnknownField));
    record.headers().add(TYPE_ID_HEADER, Message.Type.MAIL_SENT.name().getBytes());
    kafkaTemplate.send(record);

    await("Send messages were received")
      .atMost(Duration.ofSeconds(5))
      .untilAsserted(() ->
      {
        Assertions
          .assertThat(messageHandler.receivedMessages[0])
          .describedAs("Expected messages were received for partition 0")
          .hasSize(3);
        Assertions
          .assertThat(messageHandler.receivedMessages[1])
          .describedAs("Expected messages were received for partition 1")
          .hasSize(3);
      });
  }

  @Test
  @DirtiesContext
  public void testPoisionPillIsSkipped()
  {
    await("Wait for service Kikeriki")
      .atMost(Duration.ofSeconds(5))
      .untilAsserted(() ->
        restTemplate.getForObject(
            "http://localhost:" + port + "/actuator/health",
            String.class
          )
          .contains("UP"));

    ProducerRecord<String, String> record;
    // The header <code>__TypeId__</code> is set by the JsonSerializer
    // on the sending side, if configured correctly

    record = new ProducerRecord<>(TOPIC, 0,"peter", read(loginMessage));
    record.headers().add(TYPE_ID_HEADER, Message.Type.LOGIN.name().getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, 1,"klaus", read(loginMessageWithUnknownField));
    record.headers().add(TYPE_ID_HEADER, Message.Type.LOGIN.name().getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, 0, "peter", "BOOM!");
    record.headers().add(TYPE_ID_HEADER, "FOO".getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, 1, "klaus", "BOOM!");
    record.headers().add(TYPE_ID_HEADER, "BAR".getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, 0, "peter", read(chatMessage));
    record.headers().add(TYPE_ID_HEADER, Message.Type.CHAT.name().getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, 1, "klaus", read(chatMessageWithUnknownField));
    record.headers().add(TYPE_ID_HEADER, Message.Type.CHAT.name().getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, 0, "peter", read(mailSentMessage));
    record.headers().add(TYPE_ID_HEADER, Message.Type.MAIL_SENT.name().getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, 1, "klaus", read(mailSentMessageWithUnknownField));
    record.headers().add(TYPE_ID_HEADER, Message.Type.MAIL_SENT.name().getBytes());
    kafkaTemplate.send(record);

    await("Send messages were received")
      .atMost(Duration.ofSeconds(5))
      .untilAsserted(() ->
      {
        Assertions
          .assertThat(messageHandler.receivedMessages[0])
          .describedAs("Expected messages were received for partition 0")
          .hasSize(3);
        Assertions
          .assertThat(messageHandler.receivedMessages[1])
          .describedAs("Expected messages were received for partition 1")
          .hasSize(3);
      });
  }


  static class TestMessageHandler implements MessageHandler
  {
    private final List<Message> receivedMessages[];

    public TestMessageHandler(int numPartitions)
    {
      receivedMessages = new List[numPartitions];
    }

    @Override
    public void handle(int partition, long offset, Message message)
    {
      receivedMessages[partition].add(message);
    }

    @Override
    public void addPartition(int partition, long endOffset)
    {
      receivedMessages[partition] = new LinkedList<>();
    }

    @Override
    public void removePartition(int partition)
    {
      receivedMessages[partition] = null;
    }
  }


  @TestConfiguration
  static class Configuration
  {
    @Bean
    @Primary
    public MessageHandler testMessageHandler()
    {
      return new TestMessageHandler(NUM_PARTITIONS);
    }
  }

  public static String read(Resource resource)
  {
    return IOUtils.toString(MessageTest.read(resource), StandardCharsets.UTF_8);
  }
}
