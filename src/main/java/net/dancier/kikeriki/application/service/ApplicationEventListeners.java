package net.dancier.kikeriki.application.service;

import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.application.domain.model.events.EmailSendingRequestedEvent;
import net.dancier.kikeriki.application.port.DancierSendMailPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ApplicationEventListeners {

  public static final Logger log = LoggerFactory.getLogger(ApplicationEventListeners.class);

  private final DancierSendMailPort sendMailPort;

  @EventListener
  public void handle(EmailSendingRequestedEvent event) {
    log.info("Handling: {}", event);
    sendMailPort.schedule(event);
  }
}
