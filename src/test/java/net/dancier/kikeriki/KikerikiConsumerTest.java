package net.dancier.kikeriki;

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

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

import static net.dancier.kikeriki.KikerikiConsumerTest.TOPIC;
import static org.awaitility.Awaitility.await;


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "dancier.kikeriki.topic=" + TOPIC,
        "dancier.kikeriki.involve-dancer-after=1s",
        "dancier.kikeriki.involvement-check-interval=10s",
        "dancier.kikeriki.reinvolvement-interval=60s" })
@EmbeddedKafka(topics = TOPIC)
public class KikerikiConsumerTest
{
  public static final String TOPIC = "FOO";

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;
  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;
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
  public void test()
  {
    await("Send messages were received")
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

    record = new ProducerRecord<>(TOPIC, "peter", read(loginMessage));
    record.headers().add("__TypeId__", "LOGIN".getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, "klaus", read(loginMessageWithUnknownField));
    record.headers().add("__TypeId__", "LOGIN".getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, "peter", read(chatMessage));
    record.headers().add("__TypeId__", "CHAT".getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, "klaus", read(chatMessageWithUnknownField));
    record.headers().add("__TypeId__", "CHAT".getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, "peter", read(mailSentMessage));
    record.headers().add("__TypeId__", "MAIL_SENT".getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, "klaus", read(mailSentMessageWithUnknownField));
    record.headers().add("__TypeId__", "MAIL_SENT".getBytes());
    kafkaTemplate.send(record);

    await("Send messages were received")
      .atMost(Duration.ofSeconds(5))
      .untilAsserted(() ->
      {
        Assertions
          .assertThat(messageHandler.receivedMessages)
          .describedAs("A message with key 'peter' has been received")
          .containsKey("peter");
        Assertions
          .assertThat(messageHandler.receivedMessages.get("peter"))
          .describedAs("Expected messages for key 'peter' were received")
          .hasSize(3);
        Assertions
          .assertThat(messageHandler.receivedMessages)
          .describedAs("A message with key 'klaus' has been received")
          .containsKey("klaus");
        Assertions
          .assertThat(messageHandler.receivedMessages.get("klaus"))
          .describedAs("Expected messages for key 'klaus' were received")
          .hasSize(3);
      });
  }


  static class TestMessageHandler implements MessageHandler
  {
    Map<String, List<Message>> receivedMessages = new HashMap<>();

    @Override
    public void handle(String key, Message message)
    {
      List<Message> messagesForKey = receivedMessages.get(key);
      if (messagesForKey == null)
      {
        messagesForKey = new LinkedList<>();
        receivedMessages.put(key, messagesForKey);
      }
      messagesForKey.add(message);
    }
  }


  @TestConfiguration
  static class Configuration
  {
    @Bean
    @Primary
    public MessageHandler testMessageHandler()
    {
      return new TestMessageHandler();
    }
  }

  public static String read(Resource resource)
  {
    return IOUtils.toString(MessageTest.read(resource), StandardCharsets.UTF_8);
  }
}
