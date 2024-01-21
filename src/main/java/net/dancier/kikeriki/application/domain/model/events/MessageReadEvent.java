package net.dancier.kikeriki.application.domain.model.events;

import lombok.Data;

@Data
public class MessageReadEvent {

  String readerId;

  String messageId;

}
