package net.dancier.kikeriki.application.port;

import lombok.Data;
import net.dancier.kikeriki.application.domain.model.state.MailMessage;
import net.dancier.kikeriki.application.domain.model.state.UnreadChatMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
public class StateDto {

  List<UnreadChatMessage> unreadChatMessages;
  private Optional<MailMessage> optSendlastMessage;

}
