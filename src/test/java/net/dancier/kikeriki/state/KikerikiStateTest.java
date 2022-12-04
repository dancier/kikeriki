package net.dancier.kikeriki.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.dancier.kikeriki.messages.MessageLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;


@ExtendWith(SpringExtension.class)
class KikerikiStateTest
{
  KikerikiState state;
  ObjectMapper mapper;

  @Value("classpath:messages/login.json")
  Resource loginMessage;

  /**
   * Sending mails to the customer should happen exactly when:
   * <p>
   * To be defined ;-)
   */

  @BeforeEach
  public void init()
  {
    state = new KikerikiState();
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
  }

  @Test
  public void test()
  {
    state.handle(read(loginMessage, MessageLogin.class));
  }


  <T> T read(Resource resource, Class<T> valueType)
  {
    try
    {
      URI uri = resource.getURI();
      File file = ResourceUtils.getFile(uri);
      return mapper.readValue(new FileInputStream(file), valueType);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
}