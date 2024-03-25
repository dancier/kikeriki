package net.dancier.kikeriki.adapter.out.mail;

import net.dancier.kikeriki.NeededInfrastructureBaseTestClass;
import net.dancier.kikeriki.application.domain.model.events.EmailSendingRequestedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


class SchedulingAndSendingMailWorks extends NeededInfrastructureBaseTestClass {

  @Autowired
  MailOutboxJpaRepository mailOutboxJpaRepository;

  @Autowired
  DancierSendMailAdapter underTest;

  @MockBean
  JavaMailSender javaMailSender;

  @Test
  public void scheduleAndSendTest() {
    List<MailOutboxJpaEntity> all = mailOutboxJpaRepository.findAll();
    assertThat(all).isEmpty();

    underTest.schedule(getDummy());

    await()
      .pollInterval(Duration.ofSeconds(2))
      .atMost(Duration.ofSeconds(10))
      .untilAsserted(
        () -> {
          verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        }
      );
  }

  private EmailSendingRequestedEvent getDummy() {
    EmailSendingRequestedEvent emailSendingRequestedEvent = new EmailSendingRequestedEvent();
    emailSendingRequestedEvent.setId(UUID.randomUUID().toString());
    return emailSendingRequestedEvent;
  }
}