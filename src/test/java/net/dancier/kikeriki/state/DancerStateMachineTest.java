package net.dancier.kikeriki.state;

import net.dancier.kikeriki.state.appevent.SendMail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class DancerStateMachineTest {

  DancerStateMachine dancerStateMachine;
  DancerStateChatMessagesRepository dancerStateChatMessagesRepository = new DancerStateChatMessagesRepository();
  DancerStateMailMessageRepository dancerStateMailMessageRepository = new DancerStateMailMessageRepository();
  @Mock
  ApplicationEventPublisher applicationEventPublisher;
  /**
   * Sending mails to the customer should happen exactly when:
   *
   * To be defined ;-)
   *
   * */

  @BeforeEach
  public void init() {
    dancerStateMachine = new DancerStateMachine(
      dancerStateChatMessagesRepository,
      dancerStateMailMessageRepository,
      applicationEventPublisher);
  }

  @Test
  public void recentUnreadMessageLeadsToScheduledRecheck() {
    UUID dancerId = UUID.randomUUID();
    Instant lastUsedTheApp = Instant.parse("2021-12-31T00:00:00Z");
    Instant lastUnreadMessage = Instant.parse("2022-01-01T00:00:00Z");
    Instant lastMailMessageSend = Instant.parse("2021-12-01T00:00:00Z");

    dancerStateChatMessagesRepository.setReadAtLeastAt(dancerId, lastUsedTheApp);
    dancerStateChatMessagesRepository.setLastUnreadMessageAt(dancerId, lastUnreadMessage);
    dancerStateMailMessageRepository.setLastMailSendAt(dancerId, lastMailMessageSend);

    dancerStateMachine.check(dancerId);

    Mockito.verify(applicationEventPublisher, times(1)).publishEvent(new SendMail());
  }

}