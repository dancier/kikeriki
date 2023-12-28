package net.dancier.kikeriki.adapter.out.mail;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;

@RequiredArgsConstructor
@Component
public class SendMailJob {

  private static final Logger log = LoggerFactory.getLogger(SendMailJob.class);

  private final MailOutboxJpaRepository mailOutboxJpaRepository;

  private final JavaMailSender javaMailSender;

  @Transactional
  @Scheduled(fixedRate = 2000)
  public void sendMails() {
    log.info("Checking...");
    Collection<MailOutboxJpaEntity> itemsToBeSend = mailOutboxJpaRepository.lockAndList();
    for(MailOutboxJpaEntity item: itemsToBeSend) {
      sendMail(item);
    }
  }

  private void sendMail(MailOutboxJpaEntity item) {
    log.info("Sending the mail via SMTP: {}", item);
    javaMailSender.send(item.getEmailSendingRequestedEvent());
  }

}
