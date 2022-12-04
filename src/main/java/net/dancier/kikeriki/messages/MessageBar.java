package net.dancier.kikeriki.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageBar extends Message
{
  private String bar;


  @Override
  public Type getType()
  {
    return Type.BAR;
  }
}
