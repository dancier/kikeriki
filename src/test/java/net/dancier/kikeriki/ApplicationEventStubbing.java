package net.dancier.kikeriki;

import net.dancier.kikeriki.application.domain.model.messages.MessagePostedEvent;
import net.dancier.kikeriki.application.domain.model.messages.MessageReadEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ApplicationEventStubbing {

  public static final String MESSAGE_POSTED_ID = "0e64785a-ea9e-11ee-a51a-47ce2e5acd89";
  public static final LocalDateTime MESSAGE_POSTED_CREATED_AT = LocalDateTime.of(2022, 3, 25, 10, 0);
  public static final String RECIPIENT_ID = "6b9be51c-ea9e-11ee-b1fe-6fc856b3636f";
  public static final String AUTHOR_ID = "8cfaf6b2-ea9e-11ee-a9f4-1b5a676bd863";

  public static MessagePostedEvent messagePostedEvent() {
    MessagePostedEvent messagePostedEvent = new MessagePostedEvent();
    messagePostedEvent.setMessageId(MESSAGE_POSTED_ID);
    messagePostedEvent.setCreatedAt(MESSAGE_POSTED_CREATED_AT);
    messagePostedEvent.setRecipients(List.of(RECIPIENT_ID));
    messagePostedEvent.setAuthorId(AUTHOR_ID);
    return messagePostedEvent;
  }
  public static MessageReadEvent messageReadEvent() {
    return null;
  }

}
