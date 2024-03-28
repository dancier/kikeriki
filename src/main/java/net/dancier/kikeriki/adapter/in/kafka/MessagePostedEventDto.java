package net.dancier.kikeriki.adapter.in.kafka;

import java.time.OffsetDateTime;
import java.util.List;

public class MessagePostedEventDto {

  public String messageId;

  public String authorId;
  public List<String> participantIds;

  public OffsetDateTime createdAt;

}
