package net.dancier.kikeriki.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.UUID;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageFoo extends Message
{
  UUID dancerId;
  private String foo;


  @Override
  public Type getType()
  {
    return Type.FOO;
  }
}
