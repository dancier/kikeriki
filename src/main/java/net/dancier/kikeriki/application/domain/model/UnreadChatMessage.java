package net.dancier.kikeriki.application.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UnreadChatMessage {

  @EqualsAndHashCode.Include
  private String chatMessageId;
  private LocalDateTime createdAt;

  public static UnreadChatMessage of(@NonNull String chatMessageId, @NonNull LocalDateTime createdAt) {
    return new UnreadChatMessage(chatMessageId, createdAt);
  }
}
