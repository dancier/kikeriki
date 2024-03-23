package net.dancier.kikeriki;


import net.dancier.kikeriki.adapter.out.mail.MailOutboxJpaEntity;
import net.dancier.kikeriki.adapter.out.mail.MailOutboxJpaRepository;
import net.dancier.kikeriki.application.domain.model.events.EmailSendingRequestedEvent;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
@EmbeddedKafka(        brokerProperties={
  "log.dir=out/embedded-kafka"
})

public class MailSystemTest extends AbstractPostgreSQLEnabledTest{

  @Autowired
  MailOutboxJpaRepository mailOutboxJpaRepository;

  @Test
  public void test() {
      List<MailOutboxJpaEntity> all = mailOutboxJpaRepository.findAll();
      then(all).isEmpty();
      MailOutboxJpaEntity mailOutboxJpaEntity = getDummyMailOutboxJpaEntity();

      mailOutboxJpaRepository.save(mailOutboxJpaEntity);

      all = mailOutboxJpaRepository.findAll();

      then(all).isNotEmpty();
      then(all.getFirst().getId()).isNotNull();
  }

  private MailOutboxJpaEntity getDummyMailOutboxJpaEntity() {
    EmailSendingRequestedEvent emailSendingRequestedEvent = new EmailSendingRequestedEvent();
    emailSendingRequestedEvent.setId(UUID.randomUUID().toString());
    emailSendingRequestedEvent.setTo("foo");
    emailSendingRequestedEvent.setCc("bar");
    emailSendingRequestedEvent.setBcc("foo");
    emailSendingRequestedEvent.setText("hall welt");
    MailOutboxJpaEntity entity = new MailOutboxJpaEntity();
    entity.setId(emailSendingRequestedEvent.getId());
    entity.setEmailSendingRequestedEvent(emailSendingRequestedEvent);
    entity.setStatus(MailOutboxJpaEntity.STATUS.NEW);
    entity.setCreatedAt(LocalDateTime.now());

    return entity;
  }

}
