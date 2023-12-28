package net.dancier.kikeriki.adapter.out.mail;

import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.application.domain.model.events.EmailSendingRequestedEvent;
import net.dancier.kikeriki.application.port.DancierSendMailPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DancierDancierSendMailAdapter implements DancierSendMailPort {

  public static final Logger log = LoggerFactory.getLogger(DancierDancierSendMailAdapter.class);

  @Override
  public void schedule(EmailSendingRequestedEvent emailSendingRequestedEvent) {
      log.info("Storing message for later delivery: {}", emailSendingRequestedEvent);
  }
}
