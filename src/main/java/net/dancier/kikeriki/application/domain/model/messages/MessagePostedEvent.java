package net.dancier.kikeriki.application.domain.model.messages;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MessagePostedEvent {

  @NotEmpty
  String messageId;

  @NotEmpty
  String authorId;

  @NotEmpty
  List<String> recipients;

  @NotNull
  LocalDateTime createdAd;

}
