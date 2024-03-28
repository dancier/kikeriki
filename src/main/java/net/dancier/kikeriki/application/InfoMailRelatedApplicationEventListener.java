package net.dancier.kikeriki.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.application.domain.model.messages.MessagePostedEvent;
import net.dancier.kikeriki.application.domain.model.messages.MessageReadEvent;
import net.dancier.kikeriki.application.domain.model.state.State;
import net.dancier.kikeriki.application.domain.model.state.UnreadChatMessage;
import net.dancier.kikeriki.application.port.ScheduleInfomailCheckPort;
import net.dancier.kikeriki.application.port.StatePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;

@RequiredArgsConstructor
@Component
public class InfoMailRelatedApplicationEventListener {

  public static final Logger log = LoggerFactory.getLogger(InfoMailRelatedApplicationEventListener.class);

  private final StatePort statePort;

  private final ScheduleInfomailCheckPort scheduleInfomailCheckPort;

  @EventListener
  @Transactional
  public void handle(MessagePostedEvent messagePostedEvent) {
    for (String recipientId: messagePostedEvent.getRecipients()) {
      State state = statePort.get(recipientId);
      state.addUnreadChatMessage(UnreadChatMessage.of(messagePostedEvent.getMessageId(), messagePostedEvent.getCreatedAt()));
      scheduleInfomailCheckPort.schedule(LocalDateTime.now().plusMinutes(30),recipientId);
      statePort.save(state.toDto(),recipientId);
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
