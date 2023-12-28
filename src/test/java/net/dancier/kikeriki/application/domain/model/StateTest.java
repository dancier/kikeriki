package net.dancier.kikeriki.application.domain.model;

import net.dancier.kikeriki.application.domain.model.state.MailMessage;
import net.dancier.kikeriki.application.domain.model.state.State;
import net.dancier.kikeriki.application.domain.model.state.UnreadChatMessage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class StateTest {

  @Test
  public void testTimeOfLastMailMessage() {
    State state = new State();

    // no message has been sent so the last MessageSentTimeStamp must by Optional.empty
    assertThat(state.getLastTimeMailWasSent()).isEmpty();

    LocalDateTime now = LocalDateTime.now();
    MailMessage mailMessage = MailMessage.of(now);
    state.setLastMessage(mailMessage);

    assertThat(state.getLastTimeMailWasSent()).isNotEmpty();
    assertThat(state.getLastTimeMailWasSent().get()).isEqualTo(now);

    assertThatThrownBy(() -> {
      state.setLastMessage(null);
    }).isInstanceOf(NullPointerException.class);
  }

  @Test
  public void testOfAddingChatMessages() {
    State state = new State();
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime onHourLater = now.plusHours(1);
    LocalDateTime twoHoursLater = now.plusHours(2);

    UnreadChatMessage oldUnreadChatMessage = UnreadChatMessage.of(UUID.randomUUID().toString(), now);
    state.addUnreadChatMessage(oldUnreadChatMessage);

    assertThat(state.hasUnreadMessagesAfter(now)).isFalse();

    UnreadChatMessage newUnreadChatMessage = UnreadChatMessage.of(UUID.randomUUID().toString(), twoHoursLater);
    state.addUnreadChatMessage(newUnreadChatMessage);

    assertThat(state.hasUnreadMessagesAfter(now)).isTrue();
  }

  @Test
  public void testRemoveReadMessages() {
    State state = new State();
    String idOfOneMessage = UUID.randomUUID().toString();
    String idOfAnotherMessage = UUID.randomUUID().toString();

    UnreadChatMessage oneUnreadChatMessage = UnreadChatMessage
      .of(idOfOneMessage, LocalDateTime.now());

    UnreadChatMessage anotherUnreadChatMessage = UnreadChatMessage
      .of(idOfAnotherMessage, LocalDateTime.now());

    assertThat(state.unreadMessagesCount()).isEqualTo(0);

    state.addUnreadChatMessage(oneUnreadChatMessage);
    state.addUnreadChatMessage(anotherUnreadChatMessage);

    assertThat(state.unreadMessagesCount()).isEqualTo(2);

    state.removeReadMessages(idOfAnotherMessage);

    assertThat(state.unreadMessagesCount()).isEqualTo(1);

    // remove the same message twice does not change anything
    state.removeReadMessages(idOfAnotherMessage);

    assertThat(state.unreadMessagesCount()).isEqualTo(1);

  }

  @Test
  public void addUnreadMessage() {
    State state = new State();

    String unreadMessageId = UUID.randomUUID().toString();

    UnreadChatMessage unreadChatMessage = UnreadChatMessage.of(unreadMessageId, LocalDateTime.now());
    UnreadChatMessage unreadChatMessageWithSameId = UnreadChatMessage.of(unreadMessageId, LocalDateTime.now());

    assertThat(state.unreadMessagesCount()).isEqualTo(0);

    state.addUnreadChatMessage(unreadChatMessage);

    assertThat(state.unreadMessagesCount()).isEqualTo(1);

    state.addUnreadChatMessage(unreadChatMessageWithSameId);

    assertThat(state.unreadMessagesCount()).isEqualTo(1);
  }

  @Test
  public void allowedToSendAMail() {
    State state = new State();

    assertThat(state.allowedToSendAnotherMail(LocalDate.now())).isTrue();

    MailMessage mailMessage = MailMessage.of(LocalDateTime.now());
    state.setLastMessage(mailMessage);

    assertThat(state.allowedToSendAnotherMail(LocalDate.now())).isFalse();

    assertThat(state.allowedToSendAnotherMail(LocalDate.now().plusDays(1))).isTrue();

  }
}