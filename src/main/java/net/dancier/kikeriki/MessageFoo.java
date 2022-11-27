package net.dancier.kikeriki;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageFoo extends Message
{
  private String foo;


  @Override
  public Type getType()
  {
    return Type.FOO;
  }
}
