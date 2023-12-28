package net.dancier.kikeriki.adapter.out.mail;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SendMailJob {

  private static final Logger log = LoggerFactory.getLogger(SendMailJob.class);

  private final MailOutboxJpaRepository mailOutboxJpaRepository;

  private final JavaMailSender javaMailSender;

  @Scheduled(fixedRate = 2000)
  public void sendMails() {
    log.info("Checking...");
    Collection<MailOutboxJpaEntity> itemsToBeSend = mailOutboxJpaRepository.lockAndList();
    log.info("Got this: {}", itemsToBeSend);
    for(MailOutboxJpaEntity item: itemsToBeSend) {
      sendMail(item);
      item.setStatus(MailOutboxJpaEntity.STATUS.DONE);
    }
  }

  @Transactional
  private void sendMail(MailOutboxJpaEntity item) {
    log.info("Sending the mail via SMTP: {}", item);
    javaMailSender.send(item.getEmailSendingRequestedEvent());
    item.setStatus(MailOutboxJpaEntity.STATUS.DONE);
    mailOutboxJpaRepository.save(item);
  }

}
