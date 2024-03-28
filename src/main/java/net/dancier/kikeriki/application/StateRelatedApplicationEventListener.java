package net.dancier.kikeriki.application;

import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.application.domain.model.messages.MessagePostedEvent;
import net.dancier.kikeriki.application.domain.model.messages.MessageReadEvent;
import net.dancier.kikeriki.application.domain.model.state.State;
import net.dancier.kikeriki.application.domain.model.state.UnreadChatMessage;
import net.dancier.kikeriki.application.port.StatePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class StateRelatedApplicationEventListener {

  public static final Logger log = LoggerFactory.getLogger(StateRelatedApplicationEventListener.class);

  private final StatePort statePort;

  @EventListener
  public void handle(MessagePostedEvent messagePostedEvent) {
    for (String recipientId: messagePostedEvent.getRecipients()) {
      State state = statePort.get(recipientId);
      state.addUnreadChatMessage(UnreadChatMessage.of(messagePostedEvent.getMessageId(), messagePostedEvent.getCreatedAt()));
      statePort.save(state.toDto(),recipientId);
      // schedule check in 30 minutes
    }
  }

  @EventListener
  public void handle(MessageReadEvent messageReadEvent) {
    log.info("Handling Read.... {}", messageReadEvent);
    State state = statePort.get(messageReadEvent.getReaderId());
    state.removeReadMessages(messageReadEvent.getMessageId());
    statePort.save(state.toDto(), messageReadEvent.getReaderId());
  }
}
