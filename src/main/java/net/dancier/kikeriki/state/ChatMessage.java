package net.dancier.kikeriki.state;

import lombok.Data;

import java.util.UUID;

@Data
public class ChatMessage {

  UUID dancerId;

  ChatMessageStatus status;
  public static enum ChatMessageStatus {
    NEW,
    READ
  }
}
