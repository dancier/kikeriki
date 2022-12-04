package net.dancier.kikeriki.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageMailSent extends Message
{
  UUID dancerId;
  ZonedDateTime time;


  @Override
  public Message.Type getType()
  {
    return Message.Type.MAIL_SENT;
  }
}
