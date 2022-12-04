package net.dancier.kikeriki;

import net.dancier.kikeriki.messages.*;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.time.Duration;
import java.util.*;

import static net.dancier.kikeriki.KikerikiApplicationTests.TOPIC;
import static org.awaitility.Awaitility.await;


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "dancier.kikeriki.topic=" + TOPIC,
        "dancier.kikeriki.involve-dancer-after=1s",
        "dancier.kikeriki.involvement-check-interval=10s" })
@EmbeddedKafka(topics = TOPIC)
public class KikerikiApplicationTests
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


  @Test
  public void test()
  {
    restTemplate.getForObject(
        "http://localhost:" + port + "/actuator/health",
        String.class
        )
        .contains("UP");

    ProducerRecord<String, String> record;
    // The header <code>__TypeId__</code> is set by the JsonSerializer
    // on the sending side, if configured correctly

    record = new ProducerRecord<>(TOPIC, "peter", "{\"foo\":\"1\"}");
    record.headers().add("__TypeId__", "FOO".getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, "peter", "{\"foo\":\"2\"}");
    record.headers().add("__TypeId__", "FOO".getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, "peter", "{\"bar\":\"3\"}");
    record.headers().add("__TypeId__", "BAR".getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, "peter", "{\"bar\":\"4\"}");
    record.headers().add("__TypeId__", "BAR".getBytes());
    kafkaTemplate.send(record);

    await("Send messages were received")
      .atMost(Duration.ofSeconds(5))
      .untilAsserted(() ->
      {
        Assertions
          .assertThat(messageHandler.receivedFooMessages)
          .describedAs("A message of type FOO with key 'peter' has been received")
          .containsKey("peter");
        Assertions
          .assertThat(messageHandler.receivedFooMessages.get("peter"))
          .describedAs("Tow messages of type FOO has been received for key 'peter'")
          .hasSize(2);
        Assertions
          .assertThat(messageHandler.receivedFooMessages.get("peter").get(0).getFoo())
          .describedAs("The first message of type FOO for peter has the expected value")
          .isEqualTo("1");
        Assertions
          .assertThat(messageHandler.receivedFooMessages.get("peter").get(1).getFoo())
          .describedAs("The second message of type FOO for peter has the expected value")
          .isEqualTo("2");
        Assertions
          .assertThat(messageHandler.receivedBarMessages)
          .describedAs("A message of type BAR with key 'peter' has been received")
          .containsKey("peter");
        Assertions
          .assertThat(messageHandler.receivedBarMessages.get("peter"))
          .describedAs("Tow messages of type BAR has been received for key 'peter'")
          .hasSize(2);
        Assertions
          .assertThat(messageHandler.receivedBarMessages.get("peter").get(0).getBar())
          .describedAs("The first message of type BAR for peter has the expected value")
          .isEqualTo("3");
        Assertions
          .assertThat(messageHandler.receivedBarMessages.get("peter").get(1).getBar())
          .describedAs("The second mssage of type BAR for peter has the expected value")
          .isEqualTo("4");
      });
  }


  static class TestMessageHandler implements MessageHandler
  {
    Map<String, List<MessageFoo>> receivedFooMessages = new HashMap<>();
    Map<String, List<MessageBar>> receivedBarMessages = new HashMap<>();
    Map<String, List<MessageLogin>> receivedLoginMessages = new HashMap<>();
    Map<String, List<MessageChat>> receivedChatMessages = new HashMap<>();
    Map<String, List<MessageMailSent>> receivedMailSentMessages = new HashMap<>();

    @Override
    public void handle(String key, Message message)
    {
      switch (message.getType())
      {
        case FOO:
          handle(key, (MessageFoo)message);
          break;
        case BAR:
          handle(key, (MessageBar)message);
          break;
        case LOGIN:
          handle(key, (MessageLogin)message);
          break;
        case CHAT:
          handle(key, (MessageChat)message);
          break;
        case MAIL_SENT:
          handle(key, (MessageMailSent)message);
          break;
        default:
          throw new RuntimeException("Received message of unknown type: " + message);
      }
    }

    public void handle(String key, MessageFoo foo)
    {
      List<MessageFoo> messagesForKey = receivedFooMessages.get(key);
      if (messagesForKey == null)
      {
        messagesForKey = new LinkedList<>();
        receivedFooMessages.put(key, messagesForKey);
      }
      messagesForKey.add(foo);
    }

    public void handle(String key, MessageBar bar)
    {
      List<MessageBar> messagesForKey = receivedBarMessages.get(key);
      if (messagesForKey == null)
      {
        messagesForKey = new LinkedList<>();
        receivedBarMessages.put(key, messagesForKey);
      }
      messagesForKey.add(bar);
    }

    public void handle(String key, MessageLogin login)
    {
      List<MessageLogin> messagesForKey = receivedLoginMessages.get(key);
      if (messagesForKey == null)
      {
        messagesForKey = new LinkedList<>();
        receivedLoginMessages.put(key, messagesForKey);
      }
      messagesForKey.add(login);
    }

    public void handle(String key, MessageChat chat)
    {
      List<MessageChat> messagesForKey = receivedChatMessages.get(key);
      if (messagesForKey == null)
      {
        messagesForKey = new LinkedList<>();
        receivedChatMessages.put(key, messagesForKey);
      }
      messagesForKey.add(chat);
    }

    public void handle(String key, MessageMailSent mailSent)
    {
      List<MessageMailSent> messagesForKey = receivedMailSentMessages.get(key);
      if (messagesForKey == null)
      {
        messagesForKey = new LinkedList<>();
        receivedMailSentMessages.put(key, messagesForKey);
      }
      messagesForKey.add(mailSent);
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
}
