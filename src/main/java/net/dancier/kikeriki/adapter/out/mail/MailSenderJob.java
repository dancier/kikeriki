package net.dancier.kikeriki.adapter.out.mail;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static net.dancier.kikeriki.adapter.out.mail.MailOutboxJpaEntity.STATUS.*;

@RequiredArgsConstructor
@Component
public class MailSenderJob {

  private static final Logger log = LoggerFactory.getLogger(MailSenderJob.class);

  private final MailOutboxJpaRepository mailOutboxJpaRepository;

  private final JavaMailSender javaMailSender;

  @Scheduled(fixedRate = 2000)
  public void sendMails() {
    log.info("Checking...");
    Collection<MailOutboxJpaEntity> itemsToBeSend = mailOutboxJpaRepository.lockAndList();
    log.info("Got this: {}", itemsToBeSend);
    for(MailOutboxJpaEntity item: itemsToBeSend) {
      sendMail(item);
    }
  }

  private void sendMail(MailOutboxJpaEntity item) {
    log.info("Sending the mail via SMTP: {}", item);
    log.info("And sender: {}", javaMailSender);
    try {
      javaMailSender.send(item.getEmailSendingRequestedEvent());
      item.setStatus(DONE);
    } catch (MailAuthenticationException mailAuthenticationException) {
      item.setStatus(TEMPORARY_FAILED);
      log.error("Problem with password." + mailAuthenticationException.getStackTrace());
    } catch (MailException mailException) {
      item.setStatus(FINALLY_FAILED);
      log.error("Some: " + mailException);
    }
    mailOutboxJpaRepository.save(item);
  }

}
