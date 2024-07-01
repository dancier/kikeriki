package net.dancier.kikeriki.application.domain.model.messages;

import lombok.Data;

@Data
public class MessageReadEvent {

  String readerId;

  String messageId;

}
