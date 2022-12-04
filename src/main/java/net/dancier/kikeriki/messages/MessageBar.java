package net.dancier.kikeriki.messages;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.UUID;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageBar extends Message
{
  UUID dancerId;
  private String bar;


  @Override
  public Type getType()
  {
    return Type.BAR;
  }
}
