package net.dancier.kikeriki;

import io.micrometer.core.instrument.util.IOUtils;
import net.dancier.kikeriki.messages.Message;
import net.dancier.kikeriki.messages.MessageTest;
import net.dancier.kikeriki.state.DancerInvolvement;
import net.dancier.kikeriki.state.KikerikiState;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.UUID;

import static net.dancier.kikeriki.KikerikiConsumerTest.TOPIC;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
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
public class KikerikiApplicationIT
{
  public static final String TOPIC = "FOO";
  public static final String TYPE_ID_HEADER = "__TypeId__";
  public static final String UUID_DANCER = "e58ed763-928c-4155-bee9-fdbaaadc15f3";
  public static final String UUID_MESSAGE = "a58ed763-728c-9355-b339-3db21adc15a3";
  public static final String LAST_LOGIN = "2021-12-31T00:00:00+01:00[Europe/Berlin]";
  public static final String MESSAG_READ = "2022-01-03T00:00:00+01:00[Europe/Berlin]";
  public static final String MAIL_SENT = "2022-01-04T00:00:00+01:00[Europe/Berlin]";

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;
  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  private KikerikiState kikerikiState;

  @Value("classpath:messages/login.json")
  Resource loginMessage;
  @Value("classpath:messages/chat.json")
  Resource chatMessageNewMessage;
  @Value("classpath:messages/chat-with-unknown-field.json")
  Resource chatMessageMessageRead;
  @Value("classpath:messages/mail-sent.json")
  Resource mailSentMessage;


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

    record = new ProducerRecord<>(TOPIC, UUID_DANCER, read(loginMessage));
    record.headers().add(TYPE_ID_HEADER, Message.Type.LOGIN.name().getBytes());
    kafkaTemplate.send(record);
    record = new ProducerRecord<>(TOPIC, UUID_DANCER, read(chatMessageNewMessage));
    record.headers().add(TYPE_ID_HEADER, Message.Type.CHAT.name().getBytes());
    kafkaTemplate.send(record);

    await("Expected Kikeriki-State after unread chat-message")
      .atMost(Duration.ofSeconds(5))
      .untilAsserted(() ->
      {
        assertThat(
          kikerikiState
            .getDancerInvolvements()
            .filter(involvement -> involvement.getDancerId().toString().equals(UUID_DANCER)))
          .describedAs("Wrong number of involvments for dancer " + UUID_DANCER)
          .hasSize(1);
        assertThat(
          kikerikiState
            .getDancerInvolvements()
            .filter(involvement -> involvement.getDancerId().toString().equals(UUID_DANCER))
            .findFirst()
            .get()
            .getLastInvolvement())
          .describedAs("The last involvment of the dancer should be her/his last login")
          .isEqualTo(ZonedDateTime.parse(LAST_LOGIN));
        assertThat(
          kikerikiState
            .getDancerInvolvements()
            .filter(involvement -> involvement.getDancerId().toString().equals(UUID_DANCER))
            .findFirst()
            .get()
            .getLastMailSent())
          .describedAs("No mails should have been sent to the dancer yet")
          .isEqualTo(DancerInvolvement.NEVER);
        assertThat(
          kikerikiState
            .getDancerInvolvements()
            .filter(involvement -> involvement.getDancerId().toString().equals(UUID_DANCER))
            .findFirst()
            .get()
            .getUnseenMessages())
          .describedAs("Unexpected unseen chat-messages")
          .containsExactlyInAnyOrder(UUID.fromString(UUID_MESSAGE));
      });

    record = new ProducerRecord<>(TOPIC, UUID_DANCER, read(chatMessageMessageRead));
    record.headers().add(TYPE_ID_HEADER, Message.Type.CHAT.name().getBytes());
    kafkaTemplate.send(record);

    await("Expected Kikeriki-State after chat-message was read")
      .atMost(Duration.ofSeconds(5))
      .untilAsserted(() ->
      {
        assertThat(
          kikerikiState
            .getDancerInvolvements()
            .filter(involvement -> involvement.getDancerId().toString().equals(UUID_DANCER)))
          .describedAs("Wrong number of involvments for dancer " + UUID_DANCER)
          .hasSize(1);
        assertThat(
          kikerikiState
            .getDancerInvolvements()
            .filter(involvement -> involvement.getDancerId().toString().equals(UUID_DANCER))
            .findFirst()
            .get()
            .getLastInvolvement())
          .describedAs("The last involvement of the dancer should be the reading of the chat-message")
          .isEqualTo(ZonedDateTime.parse(MESSAG_READ));
        assertThat(
          kikerikiState
            .getDancerInvolvements()
            .filter(involvement -> involvement.getDancerId().toString().equals(UUID_DANCER))
            .findFirst()
            .get()
            .getLastMailSent())
          .describedAs("No mails should have been sent to the dancer yet")
          .isEqualTo(DancerInvolvement.NEVER);
        assertThat(
          kikerikiState
            .getDancerInvolvements()
            .filter(involvement -> involvement.getDancerId().toString().equals(UUID_DANCER))
            .findFirst()
            .get()
            .getUnseenMessages())
          .describedAs("Unexpected unseen chat-messages")
          .isEmpty();
      });

    record = new ProducerRecord<>(TOPIC, UUID_DANCER, read(mailSentMessage));
    record.headers().add(TYPE_ID_HEADER, Message.Type.MAIL_SENT.name().getBytes());
    kafkaTemplate.send(record);

    await("Expected Kikeriki-State after an invovement mail was sent")
      .atMost(Duration.ofSeconds(5))
      .untilAsserted(() ->
      {
        assertThat(
          kikerikiState
            .getDancerInvolvements()
            .filter(involvement -> involvement.getDancerId().toString().equals(UUID_DANCER)))
          .describedAs("Wrong number of involvments for dancer " + UUID_DANCER)
          .hasSize(1);
        assertThat(
          kikerikiState
            .getDancerInvolvements()
            .filter(involvement -> involvement.getDancerId().toString().equals(UUID_DANCER))
            .findFirst()
            .get()
            .getLastInvolvement())
          .describedAs("The last involvement of the dancer should be the reading of the chat-message")
          .isEqualTo(ZonedDateTime.parse(MESSAG_READ));
        assertThat(
          kikerikiState
            .getDancerInvolvements()
            .filter(involvement -> involvement.getDancerId().toString().equals(UUID_DANCER))
            .findFirst()
            .get()
            .getLastMailSent())
          .describedAs("Unexpected timestamp for the last sent mail")
          .isEqualTo(ZonedDateTime.parse(MAIL_SENT));
        assertThat(
          kikerikiState
            .getDancerInvolvements()
            .filter(involvement -> involvement.getDancerId().toString().equals(UUID_DANCER))
            .findFirst()
            .get()
            .getUnseenMessages())
          .describedAs("Unexpected unseen chat-messages")
          .isEmpty();
      });
  }

  public static String read(Resource resource)
  {
    return IOUtils.toString(MessageTest.read(resource), StandardCharsets.UTF_8);
  }
}
