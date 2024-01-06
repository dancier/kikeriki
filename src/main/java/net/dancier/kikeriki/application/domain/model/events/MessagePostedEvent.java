package net.dancier.kikeriki.application.domain.model.events;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class MessagePostedEvent {

  @NotNull
  String messageId;

  @NotEmpty
  List<UUID> recipients;

  @NotNull
  LocalDateTime createdAd;

}
