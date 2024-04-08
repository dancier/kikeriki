package net.dancier.kikeriki.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.adapter.out.userinfo.UserInfoAdapter;
import net.dancier.kikeriki.application.domain.model.messages.EmailSendingRequestedCommand;
import net.dancier.kikeriki.application.domain.model.state.InfoMail;
import net.dancier.kikeriki.application.domain.model.state.State;
import net.dancier.kikeriki.application.port.StatePort;
import net.dancier.kikeriki.application.port.UserInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class CheckAndSendService {
  private final static Logger log = LoggerFactory.getLogger(CheckAndSendService.class);
  public final static String USER_INFO_MAIL = "text/user-info";

  private final StatePort statePort;

  private final UserInfoAdapter userInfoAdapter;

  private final ApplicationEventPublisher applicationEventPublisher;

  private final TemplateEngine templateEngine;

  @Transactional
  public void checkAndSend(String dancerId) {
    log.info("CheckAndSend for {}", dancerId);
    State state = statePort.get(dancerId);
    if (state.isCandidateForSendingMail(LocalDateTime.now())) {
      sendMail(dancerId, state);
      statePort.save(state.toDto(), dancerId);
    }
  }
  public void sendMail(String dancerId, State state) {
    log.info("Sending InfoMail...");
    UserInfoDto userInfoDto =  userInfoAdapter.loadByDancerId(dancerId);
    String emailAddress = userInfoDto.getEmailAddress();
    EmailSendingRequestedCommand emailSendingRequestedCommand =
      new EmailSendingRequestedCommand.EmailSendingRequestedCommandBuilder()
        .setFrom("marc@gorzala.de")
        .setId(UUID.randomUUID().toString())
        .setTo(new String[]{emailAddress})
        .setSubject("Neues für Dich auf Dancier.net")
        .setText(createBody(state, userInfoDto))
        .build();
    applicationEventPublisher.publishEvent(emailSendingRequestedCommand);
    state.setLastMailMessage(InfoMail.of(LocalDateTime.now()));
  }

  private String createBody(State state, UserInfoDto userInfoDto) {
    Map<String, Object> map = new HashMap<>();
    map.put("unread_messages", state.unreadMessagesCount());
    map.put("dancer_name", userInfoDto.getDancerName());
    Context context = new Context(Locale.GERMANY, map);
    return templateEngine.process(USER_INFO_MAIL, context);
  }
}
