package net.dancier.kikeriki.application.service;

import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.application.domain.model.events.EmailSendingRequestedEvent;
import net.dancier.kikeriki.application.domain.model.events.MessagePostedEvent;
import net.dancier.kikeriki.application.domain.model.state.State;
import net.dancier.kikeriki.application.domain.model.state.UnreadChatMessage;
import net.dancier.kikeriki.application.port.DancierSendMailPort;
import net.dancier.kikeriki.application.port.StatePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class ApplicationEventListeners {

  private final StatePort statePort;

  public static final Logger log = LoggerFactory.getLogger(ApplicationEventListeners.class);

  private final DancierSendMailPort sendMailPort;


  @EventListener
  public void handle(EmailSendingRequestedEvent event) {
    log.info("Handling: {}", event);
    sendMailPort.schedule(event);
  }

  @EventListener
  public void handle(MessagePostedEvent messagePostedEvent) {
    log.info("Handling MessagePostedEvent: " + messagePostedEvent);
    State state = new State();

    statePort.save(state, UUID.randomUUID().toString());
    /*var unreadChatMessage = UnreadChatMessage.of(messagePostedEvent.getMessageId(),messagePostedEvent.getCreatedAd());
    List<UUID> recipients =  messagePostedEvent.getRecipients();
    for(UUID recipientId : recipients) {
      var recipientState = statePort.get(recipientId);
      recipientState.addUnreadChatMessage(unreadChatMessage);
      statePort.save(state, UUID.randomUUID().toString());
    }*/
  }
}
