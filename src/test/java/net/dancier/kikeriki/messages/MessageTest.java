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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


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
  @DisplayName("Deserialize a MessageFoo message")
  public void testDeserializeMessageFoo()
  {
    assertDoesNotThrow(() -> mapper.readValue(read(fooMessage), MessageFoo.class));
    assertDoesNotThrow(() -> mapper.readValue(read(fooMessageWithUnknownField), MessageFoo.class));
  }

  @Test
  @DisplayName("Deserialize a MessageBar message")
  public void testDeserializeMessageBar()
  {
    assertDoesNotThrow(() -> mapper.readValue(read(barMessage), MessageBar.class));
    assertDoesNotThrow(() -> mapper.readValue(read(barMessageWithUnknownField), MessageBar.class));
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
