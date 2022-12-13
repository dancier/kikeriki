package net.dancier.kikeriki;

import net.dancier.kikeriki.messages.MessageChat;
import net.dancier.kikeriki.messages.MessageLogin;
import net.dancier.kikeriki.messages.MessageMailSent;
import net.dancier.kikeriki.state.DancerInvolvement;
import net.dancier.kikeriki.state.KikerikiState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class InvolveDancersMessageHandlerTest
{
  @Test
  @DisplayName("Handling of a login-message, if ivolvement is disabled")
  public void testHandleLoginMessageWithInvolvementDisabled(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver,
    @Mock DancerInvolvement involvement)
  {
    InvolveDancersMessageHandler handler = new InvolveDancersMessageHandler(state, involver);
    handler.setInvolvementEnabled(false);

    // Given
    MessageLogin message = new MessageLogin();
    ZonedDateTime time = ZonedDateTime.now();
    message.setTime(time);
    when(state.handle(any(MessageLogin.class))).thenReturn(involvement);

    // When
    handler.handle("foo", message);

    // Then
    verify(state, times(1)).handle(message);
    verify(involver, never()).involveDancer(any(DancerInvolvement.class), any(ZonedDateTime.class));
    verify(involver, never()).involveOtherDancers(any(ZonedDateTime.class));
  }

  @Test
  @DisplayName("Handling of a login-message, if ivolvement is enabled")
  public void testHandleLoginMessageWithInvolvementEnabled(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver,
    @Mock DancerInvolvement involvement)
  {
    InvolveDancersMessageHandler handler = new InvolveDancersMessageHandler(state, involver);
    handler.setInvolvementEnabled(true);

    // Given
    MessageLogin message = new MessageLogin();
    ZonedDateTime time = ZonedDateTime.now();
    message.setTime(time);
    when(state.handle(any(MessageLogin.class))).thenReturn(involvement);

    // When
    handler.handle("foo", message);

    // Then
    verify(state, times(1)).handle(message);
    verify(involver, times(1)).involveDancer(involvement, time);
    verify(involver, times(1)).involveOtherDancers(time);
  }

  @Test
  @DisplayName("Handling of a chat-message, if ivolvement is disabled")
  public void testHandleChatMessageWithInvolvementDisabled(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver,
    @Mock DancerInvolvement involvement)
  {
    InvolveDancersMessageHandler handler = new InvolveDancersMessageHandler(state, involver);
    handler.setInvolvementEnabled(false);

    // Given
    MessageChat message = new MessageChat();
    ZonedDateTime time = ZonedDateTime.now();
    message.setTime(time);
    when(state.handle(any(MessageChat.class))).thenReturn(involvement);

    // When
    handler.handle("foo", message);

    // Then
    verify(state, times(1)).handle(message);
    verify(involver, never()).involveDancer(any(DancerInvolvement.class), any(ZonedDateTime.class));
    verify(involver, never()).involveOtherDancers(any(ZonedDateTime.class));
  }

  @Test
  @DisplayName("Handling of a chat-message, if ivolvement is enabled")
  public void testHandleChatMessageWithInvolvementEnabled(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver,
    @Mock DancerInvolvement involvement)
  {
    InvolveDancersMessageHandler handler = new InvolveDancersMessageHandler(state, involver);
    handler.setInvolvementEnabled(true);

    // Given
    MessageChat message = new MessageChat();
    ZonedDateTime time = ZonedDateTime.now();
    message.setTime(time);
    when(state.handle(any(MessageChat.class))).thenReturn(involvement);

    // When
    handler.handle("foo", message);

    // Then
    verify(state, times(1)).handle(message);
    verify(involver, times(1)).involveDancer(involvement, time);
    verify(involver, times(1)).involveOtherDancers(time);
  }

  @Test
  @DisplayName("Handling of a mail-sent-message, if ivolvement is disabled")
  public void testHandleMailSentMessageWithInvolvementDisabled(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver,
    @Mock DancerInvolvement involvement)
  {
    InvolveDancersMessageHandler handler = new InvolveDancersMessageHandler(state, involver);
    handler.setInvolvementEnabled(false);

    // Given
    MessageMailSent message = new MessageMailSent();
    ZonedDateTime time = ZonedDateTime.now();
    message.setTime(time);
    when(state.handle(any(MessageMailSent.class))).thenReturn(involvement);

    // When
    handler.handle("foo", message);

    // Then
    verify(state, times(1)).handle(message);
    verify(involver, never()).involveDancer(any(DancerInvolvement.class), any(ZonedDateTime.class));
    verify(involver, never()).involveOtherDancers(any(ZonedDateTime.class));
  }

  @Test
  @DisplayName("Handling of a mail-sent-message, if ivolvement is enabled")
  public void testHandleMailSentMessageWithInvolvementEnabled(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver,
    @Mock DancerInvolvement involvement)
  {
    InvolveDancersMessageHandler handler = new InvolveDancersMessageHandler(state, involver);
    handler.setInvolvementEnabled(true);

    // Given
    MessageMailSent message = new MessageMailSent();
    ZonedDateTime time = ZonedDateTime.now();
    message.setTime(time);
    when(state.handle(any(MessageMailSent.class))).thenReturn(involvement);

    // When
    handler.handle("foo", message);

    // Then
    verify(state, times(1)).handle(message);
    verify(involver, never()).involveDancer(any(DancerInvolvement.class), any(ZonedDateTime.class));
    verify(involver, times(1)).involveOtherDancers(time);
  }
}
