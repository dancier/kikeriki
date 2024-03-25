package net.dancier.kikeriki.adapter.out.mail;

import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.application.domain.model.messages.EmailSendingRequestedCommand;
import net.dancier.kikeriki.application.port.DancierSendMailPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class DancierSendMailAdapter implements DancierSendMailPort {

  public static final Logger log = LoggerFactory.getLogger(DancierSendMailAdapter.class);

  public final MailOutboxJpaRepository mailOutboxJpaRepository;

 @Override
  public void schedule(EmailSendingRequestedCommand emailSendingRequestedCommand) {
      log.info("Storing message for later delivery: {}", emailSendingRequestedCommand);
      MailOutboxJpaEntity mailOutboxJpaEntity = new MailOutboxJpaEntity();
      mailOutboxJpaEntity.setId(emailSendingRequestedCommand.getId());
      mailOutboxJpaEntity.setStatus(MailOutboxJpaEntity.STATUS.NEW);
      mailOutboxJpaEntity.setCreatedAt(LocalDateTime.now());
      mailOutboxJpaEntity.setEmailSendingRequestedCommand(emailSendingRequestedCommand);
      mailOutboxJpaRepository.save(mailOutboxJpaEntity);
  }
}
