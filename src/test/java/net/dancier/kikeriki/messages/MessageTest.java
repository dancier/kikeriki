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
    assertThat(result.getBar())
      .describedAs("Unexpected value for field \"bar\"")
      .isEqualTo("42");
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
