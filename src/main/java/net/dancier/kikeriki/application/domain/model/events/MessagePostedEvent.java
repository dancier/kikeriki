package net.dancier.kikeriki.application.domain.model.events;

import lombok.Data;

import java.util.UUID;

@Data
public class MessagePostedEvent {

  UUID messageId;

}
