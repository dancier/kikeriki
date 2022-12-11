package net.dancier.kikeriki.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@ExtendWith(SpringExtension.class)
public class MessageTest
{
  ObjectMapper mapper;

  @Value("classpath:messages/foo.json")
  Resource fooMessage;
  @Value("classpath:messages/foo-with-unknown-field.json")
  Resource fooMessageWithUnknownField;
  @Value("classpath:messages/bar.json")
  Resource barMessage;
  @Value("classpath:messages/bar-with-unknown-field.json")
  Resource barMessageWithUnknownField;
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


  @BeforeEach
  public void setUp()
  {
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
  }


  @Test
  @DisplayName("Deserialize a MessageFoo message works for valid messages")
  public void testDeserializeValidMessageFooWorks()
  {
    assertThatNoException().isThrownBy(() -> mapper.readValue(read(fooMessage), MessageFoo.class));
    assertThatNoException().isThrownBy(() -> mapper.readValue(read(fooMessageWithUnknownField), MessageFoo.class));
  }

  @Test
  @DisplayName("Deserialize a MessageFoo message yields expected results")
  public void testDeserializeValidMessageFooYieldsExpectedResults() throws IOException
  {
    MessageFoo result = mapper.readValue(read(fooMessage), MessageFoo.class);

    assertThat(result.getType())
      .describedAs("Unexpected type for message")
      .isEqualTo(Message.Type.FOO);
    assertThat(result.getDancerId())
      .describedAs("Unexpected value for field \"dancerId\"")
      .isEqualTo(UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3"));
    assertThat(result.getFoo())
      .describedAs("Unexpected value for field \"foo\"")
      .isEqualTo("42");
  }

  @Test
  @DisplayName("Deserialize a MessageBar message works for valid messages")
  public void testDeserializeValidMessageBarWorks()
  {
    assertThatNoException().isThrownBy(() -> mapper.readValue(read(barMessage), MessageBar.class));
    assertThatNoException().isThrownBy(() -> mapper.readValue(read(barMessageWithUnknownField), MessageBar.class));
  }

  @Test
  @DisplayName("Deserialize a MessageBar message yields expected results")
  public void testDeserializeValidMessageBarYieldsExpectedResults() throws IOException
  {
    MessageBar result = mapper.readValue(read(barMessage), MessageBar.class);

    assertThat(result.getType())
      .describedAs("Unexpected type for message")
      .isEqualTo(Message.Type.BAR);
    assertThat(result.getDancerId())
      .describedAs("Unexpected value for field \"dancerId\"")
      .isEqualTo(UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3"));
    assertThat(result.getBar())
      .describedAs("Unexpected value for field \"bar\"")
      .isEqualTo("42");
  }

  @Test
  @DisplayName("Deserialize a MessageChat message works for valid messages")
  public void testDeserializeValidMessageChatWorks()
  {
    assertThatNoException().isThrownBy(() -> mapper.readValue(read(chatMessage), MessageChat.class));
    assertThatNoException().isThrownBy(() -> mapper.readValue(read(chatMessageWithUnknownField), MessageChat.class));
  }

  @Test
  @DisplayName("Deserializing MessageChat messages yields expected results")
  public void testDeserializeValidMessageChatYieldsExpectedResults() throws IOException
  {
    MessageChat result;

    result = mapper.readValue(read(chatMessage), MessageChat.class);
    assertThat(result.getType())
      .describedAs("Unexpected type for message")
      .isEqualTo(Message.Type.CHAT);
    assertThat(result.getDancerId())
      .describedAs("Unexpected value for field \"dancerId\"")
      .isEqualTo(UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3"));
    assertThat(result.getTime())
      .describedAs("Unexpected value for field \"time\"")
      .isEqualTo(ZonedDateTime.parse("2021-12-31T23:00:00Z[UTC]"));
    assertThat(result.getMessageId())
      .describedAs("Unexpected value for field \"messageId\"")
      .isEqualTo(UUID.fromString("a58ed763-728c-9355-b339-3db21adc15a3"));
    assertThat(result.getStatus())
      .describedAs("Unexpected value for field \"status\"")
      .isEqualTo(MessageChat.ChatMessageStatus.NEW);

    result = mapper.readValue(read(chatMessageWithUnknownField), MessageChat.class);
    assertThat(result.getType())
      .describedAs("Unexpected type for message")
      .isEqualTo(Message.Type.CHAT);
    assertThat(result.getDancerId())
      .describedAs("Unexpected value for field \"dancerId\"")
      .isEqualTo(UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3"));
    assertThat(result.getTime())
      .describedAs("Unexpected value for field \"time\"")
      .isEqualTo(ZonedDateTime.parse("2022-01-02T23:00:00Z[UTC]"));
    assertThat(result.getMessageId())
      .describedAs("Unexpected value for field \"messageId\"")
      .isEqualTo(UUID.fromString("a58ed763-728c-9355-b339-3db21adc15a3"));
    assertThat(result.getStatus())
      .describedAs("Unexpected value for field \"status\"")
      .isEqualTo(MessageChat.ChatMessageStatus.READ);
  }

  @Test
  @DisplayName("Deserialize a MessageLogin message works for valid messages")
  public void testDeserializeValidMessageLoginWorks()
  {
    assertThatNoException().isThrownBy(() -> mapper.readValue(read(loginMessage), MessageLogin.class));
    assertThatNoException().isThrownBy(() -> mapper.readValue(read(loginMessageWithUnknownField), MessageLogin.class));
  }

  @Test
  @DisplayName("Deserializing MessageLogin messages yields expected results")
  public void testDeserializeValidMessageLoginYieldsExpectedResults() throws IOException
  {
    MessageLogin result;

    result = mapper.readValue(read(loginMessage), MessageLogin.class);
    assertThat(result.getType())
      .describedAs("Unexpected type for message")
      .isEqualTo(Message.Type.LOGIN);
    assertThat(result.getDancerId())
      .describedAs("Unexpected value for field \"dancerId\"")
      .isEqualTo(UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3"));
    assertThat(result.getTime())
      .describedAs("Unexpected value for field \"time\"")
      .isEqualTo(ZonedDateTime.parse("2021-12-30T23:00:00Z[UTC]"));

    result = mapper.readValue(read(loginMessageWithUnknownField), MessageLogin.class);
    assertThat(result.getType())
      .describedAs("Unexpected type for message")
      .isEqualTo(Message.Type.LOGIN);
    assertThat(result.getDancerId())
      .describedAs("Unexpected value for field \"dancerId\"")
      .isEqualTo(UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3"));
    assertThat(result.getTime())
      .describedAs("Unexpected value for field \"time\"")
      .isEqualTo(ZonedDateTime.parse("2021-12-30T23:00:00Z[UTC]"));
  }

  @Test
  @DisplayName("Deserialize a MessageMailSent message works for valid messages")
  public void testDeserializeValidMessageMailSentWorks()
  {
    assertThatNoException().isThrownBy(() -> mapper.readValue(read(mailSentMessage), MessageMailSent.class));
    assertThatNoException().isThrownBy(() -> mapper.readValue(read(mailSentMessageWithUnknownField), MessageMailSent.class));
  }

  @Test
  @DisplayName("Deserializing MessageMailSent messages yields expected results")
  public void testDeserializeValidMessageMailSentYieldsExpectedResults() throws IOException
  {
    MessageMailSent result;

    result = mapper.readValue(read(mailSentMessage), MessageMailSent.class);
    assertThat(result.getType())
      .describedAs("Unexpected type for message")
      .isEqualTo(Message.Type.MAIL_SENT);
    assertThat(result.getDancerId())
      .describedAs("Unexpected value for field \"dancerId\"")
      .isEqualTo(UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3"));
    assertThat(result.getTime())
      .describedAs("Unexpected value for field \"time\"")
      .isEqualTo(ZonedDateTime.parse("2022-01-03T23:00:00Z[UTC]"));

    result = mapper.readValue(read(mailSentMessageWithUnknownField), MessageMailSent.class);
    assertThat(result.getType())
      .describedAs("Unexpected type for message")
      .isEqualTo(Message.Type.MAIL_SENT);
    assertThat(result.getDancerId())
      .describedAs("Unexpected value for field \"dancerId\"")
      .isEqualTo(UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3"));
    assertThat(result.getTime())
      .describedAs("Unexpected value for field \"time\"")
      .isEqualTo(ZonedDateTime.parse("2021-12-30T23:00:00Z[UTC]"));
  }

  public static InputStream read(Resource resource)
  {
    try
    {
      URI uri = resource.getURI();
      File file = ResourceUtils.getFile(uri);
      return new FileInputStream(file);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
}
