package net.dancier.kikeriki;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class MessageTest
{
  ObjectMapper mapper = new ObjectMapper();

  @Test
  @DisplayName("Deserialize a MessageFoo message")
  public void testDeserializeMessageFoo()
  {
    Assertions.assertDoesNotThrow(() -> mapper.readValue("{\"foo\":\"42\"}", MessageFoo.class));
    Assertions.assertDoesNotThrow(() -> mapper.readValue("{\"foo\":\"42\",\"unknown-field\":666}", MessageFoo.class));
  }

  @Test
  @DisplayName("Deserialize a MessageBar message")
  public void testDeserializeMessageBar()
  {
    Assertions.assertDoesNotThrow(() -> mapper.readValue("{\"bar\":\"42\"}", MessageBar.class));
    Assertions.assertDoesNotThrow(() -> mapper.readValue("{\"bar\":\"42\",\"unknown-field\":666}", MessageBar.class));
  }
}
