package net.dancier.kikeriki.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@ExtendWith(SpringExtension.class)
public class MessageTest
{
  ObjectMapper mapper = new ObjectMapper();

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
    assertThat(result.getStatus())
      .describedAs("Unexpected value for field \"status\"")
      .isEqualTo(MessageChat.ChatMessageStatus.READ);
  }


  static InputStream read(Resource resource)
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
