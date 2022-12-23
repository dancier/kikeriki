package net.dancier.kikeriki.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageLogin extends Message
{
  UUID dancerId;
  ZonedDateTime time;

  @Override
  public Type getType()
  {
    return Type.LOGIN;
  }
}
