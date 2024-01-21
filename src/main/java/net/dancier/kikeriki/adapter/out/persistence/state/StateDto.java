package net.dancier.kikeriki.adapter.out.persistence.state;

import lombok.Data;
import net.dancier.kikeriki.application.domain.model.state.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
public class StateDto {

  List<UnreadChatMessageDto> unreadChatMessage;

  private Optional<MailMessageDto> optSendlastMessage;

  @Data
  private class UnreadChatMessageDto {
    String chatMessageId;
    LocalDateTime createdAt;
  }

  @Data
  private class MailMessageDto {
    LocalDateTime createdAt;

  }

  public static StateDto of(State state) {
    StateDto result = new StateDto();

    return result;
  }
}
