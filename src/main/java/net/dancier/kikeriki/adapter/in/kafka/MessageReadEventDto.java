package net.dancier.kikeriki.adapter.in.kafka;

import lombok.Data;

@Data
public class MessageReadEventDto {

  boolean read;
  String readerId;
  String messageId;

}
