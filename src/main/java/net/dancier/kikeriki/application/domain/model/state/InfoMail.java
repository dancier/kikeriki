package net.dancier.kikeriki.application.domain.model.state;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class InfoMail {

  LocalDateTime createdAt;

  public static InfoMail of(@NonNull LocalDateTime createdAt) {
    return new InfoMail(createdAt);
  }
}
