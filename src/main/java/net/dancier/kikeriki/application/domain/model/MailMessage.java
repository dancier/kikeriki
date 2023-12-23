package net.dancier.kikeriki.application.domain.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MailMessage {

  LocalDateTime createdAt;

  public static MailMessage of(@NonNull LocalDateTime createdAt) {
    return new MailMessage(createdAt);
  }
}
