package net.dancier.kikeriki.application.port;

import lombok.Data;
import net.dancier.kikeriki.application.domain.model.state.InfoMail;
import net.dancier.kikeriki.application.domain.model.state.UnreadChatMessage;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Data
public class StateDto {

  List<UnreadChatMessage> unreadChatMessages;
  Set<String> pendingReadMessages;
  Optional<InfoMail> optSendlastMessage;

}
