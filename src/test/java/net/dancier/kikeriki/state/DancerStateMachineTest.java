package net.dancier.kikeriki.state;

import net.dancier.kikeriki.state.appevent.SendMail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class DancerStateMachineTest {

  DancerStateMachine dancerStateMachine;
  DancerStateChatMessagesRepository dancerStateChatMessagesRepository = new DancerStateChatMessagesRepository();
  DancerStateMailMessageRepository dancerStateMailMessageRepository = new DancerStateMailMessageRepository();

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
      dancerStateMailMessageRepository);
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

    Optional<SendMail> result = dancerStateMachine.check(dancerId);

    assertThat(result.isPresent())
      .describedAs("A SendMail event should be present")
      .isTrue();
  }

}