package net.dancier.kikeriki.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageChat extends Message
{
  UUID dancerId;
  ChatMessageStatus status;


  public static enum ChatMessageStatus {
    NEW,
    READ
  }


  @Override
  public Type getType()
  {
    return Type.CHAT;
  }
}
