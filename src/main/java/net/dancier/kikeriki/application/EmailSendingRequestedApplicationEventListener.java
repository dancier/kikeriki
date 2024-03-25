package net.dancier.kikeriki.application;

import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.application.domain.model.messages.EmailSendingRequestedCommand;
import net.dancier.kikeriki.application.port.DancierSendMailPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSendingRequestedApplicationEventListener {

  private final static Logger log = LoggerFactory.getLogger(EmailSendingRequestedApplicationEventListener.class);

  private final DancierSendMailPort sendMailPort;

  @EventListener
  public void handle(EmailSendingRequestedCommand command) {
    log.info("I go a request to directly send an email. I will schedule that.: {}", command);
    sendMailPort.schedule(command);
  }

}
