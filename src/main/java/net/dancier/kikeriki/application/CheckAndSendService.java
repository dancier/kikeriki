package net.dancier.kikeriki.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.adapter.out.userinfo.UserInfoAdapter;
import net.dancier.kikeriki.application.domain.model.messages.EmailSendingRequestedCommand;
import net.dancier.kikeriki.application.domain.model.state.State;
import net.dancier.kikeriki.application.port.StatePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CheckAndSendService {
  private final Logger log = LoggerFactory.getLogger(CheckAndSendService.class);

  private final StatePort statePort;

  private final UserInfoAdapter userInfoAdapter;

  private final ApplicationEventPublisher applicationEventPublisher;

  @Transactional
  public void checkAndSend(String dancerId) {
    log.info("CheckAndSend for {}", dancerId);
    State state = statePort.get(dancerId);
    if (state.isCandidateForSendingMail(LocalDateTime.now())) {
      sendMail(dancerId);
      statePort.save(state.toDto(), dancerId);
    }
  }
  public void sendMail(String dancerId) {
    log.info("Sending InfoMail...");
    String mailAddress = userInfoAdapter.loadByDancerId(dancerId).getEmailAddress();
    EmailSendingRequestedCommand emailSendingRequestedCommand =
      new EmailSendingRequestedCommand.EmailSendingRequestedCommandBuilder()
        .setFrom("marc@gorzala.de")
        .setId(UUID.randomUUID().toString())
        .setTo(new String[]{mailAddress})
        .setSubject("Infomail")
        .setText("Hallo Welt")
        .build();
    applicationEventPublisher.publishEvent(emailSendingRequestedCommand);
  }
}
