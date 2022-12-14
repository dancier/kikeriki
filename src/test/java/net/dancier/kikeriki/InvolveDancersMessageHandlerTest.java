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
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class InvolveDancersMessageHandlerTest
{
  @Test
  @DisplayName("Handling of an outdated login-message")
  public void testHandleLoginMessageWithInvolvementDisabled(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver,
    @Mock DancerInvolvement involvement)
  {
    // Given
    InvolveDancersMessageHandler handler =
      new InvolveDancersMessageHandler(
        () -> state,
        1,
        involver);
    MessageLogin message = new MessageLogin();
    ZonedDateTime time = ZonedDateTime.now();
    message.setTime(time);
    handler.addPartition(0, 6);
    when(state.handle(any(MessageLogin.class))).thenReturn(involvement);

    // When
    handler.handle(0, 5l, message);

    // Then
    verify(state, times(1)).handle(message);
    verify(involver, never()).involveDancer(any(DancerInvolvement.class), any(ZonedDateTime.class));
    verify(involver, never()).involveOtherDancers(any(Stream.class), any(ZonedDateTime.class));
  }

  @Test
  @DisplayName("Handling of a current login-message")
  public void testHandleLoginMessageWithInvolvementEnabled(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver,
    @Mock DancerInvolvement involvement)
  {
    // Given
    InvolveDancersMessageHandler handler =
      new InvolveDancersMessageHandler(
        () -> state,
        1,
        involver);
    MessageLogin message = new MessageLogin();
    ZonedDateTime time = ZonedDateTime.now();
    message.setTime(time);
    handler.addPartition(0, 6);
    when(state.handle(any(MessageLogin.class))).thenReturn(involvement);

    // When
    handler.handle(0, 6l, message);

    // Then
    verify(state, times(1)).handle(message);
    verify(involver, times(1)).involveDancer(involvement, time);
    verify(involver, times(1)).involveOtherDancers(any(Stream.class), eq(time));
  }

  @Test
  @DisplayName("Handling of a outdated chat-message")
  public void testHandleChatMessageWithInvolvementDisabled(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver,
    @Mock DancerInvolvement involvement)
  {
    // Given
    InvolveDancersMessageHandler handler =
      new InvolveDancersMessageHandler(
        () -> state,
        1,
        involver);
    MessageChat message = new MessageChat();
    ZonedDateTime time = ZonedDateTime.now();
    message.setTime(time);
    handler.addPartition(0, 6);
    when(state.handle(any(MessageChat.class))).thenReturn(involvement);

    // When
    handler.handle(0, 5l, message);

    // Then
    verify(state, times(1)).handle(message);
    verify(involver, never()).involveDancer(any(DancerInvolvement.class), any(ZonedDateTime.class));
    verify(involver, never()).involveOtherDancers(any(Stream.class), any(ZonedDateTime.class));
  }

  @Test
  @DisplayName("Handling of a current chat-message")
  public void testHandleChatMessageWithInvolvementEnabled(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver,
    @Mock DancerInvolvement involvement)
  {
    // Given
    InvolveDancersMessageHandler handler =
      new InvolveDancersMessageHandler(
        () -> state,
        1,
        involver);
    MessageChat message = new MessageChat();
    ZonedDateTime time = ZonedDateTime.now();
    message.setTime(time);
    handler.addPartition(0, 6);
    when(state.handle(any(MessageChat.class))).thenReturn(involvement);

    // When
    handler.handle(0, 6l, message);

    // Then
    verify(state, times(1)).handle(message);
    verify(involver, times(1)).involveDancer(involvement, time);
    verify(involver, times(1)).involveOtherDancers(any(Stream.class), eq(time));
  }

  @Test
  @DisplayName("Handling of a outdated mail-sent-message")
  public void testHandleMailSentMessageWithInvolvementDisabled(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver,
    @Mock DancerInvolvement involvement)
  {
    // Given
    InvolveDancersMessageHandler handler =
      new InvolveDancersMessageHandler(
        () -> state,
        1,
        involver);
    MessageMailSent message = new MessageMailSent();
    ZonedDateTime time = ZonedDateTime.now();
    message.setTime(time);
    handler.addPartition(0, 6);
    when(state.handle(any(MessageMailSent.class))).thenReturn(involvement);

    // When
    handler.handle(0, 5l, message);

    // Then
    verify(state, times(1)).handle(message);
    verify(involver, never()).involveDancer(any(DancerInvolvement.class), any(ZonedDateTime.class));
    verify(involver, never()).involveOtherDancers(any(Stream.class), any(ZonedDateTime.class));
  }

  @Test
  @DisplayName("Handling of a current mail-sent-message")
  public void testHandleMailSentMessageWithInvolvementEnabled(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver,
    @Mock DancerInvolvement involvement)
  {
    // Given
    InvolveDancersMessageHandler handler =
      new InvolveDancersMessageHandler(
        () -> state,
        1,
        involver);
    MessageMailSent message = new MessageMailSent();
    ZonedDateTime time = ZonedDateTime.now();
    message.setTime(time);
    handler.addPartition(0, 6);
    when(state.handle(any(MessageMailSent.class))).thenReturn(involvement);

    // When
    handler.handle(0, 6l, message);

    // Then
    verify(state, times(1)).handle(message);
    verify(involver, never()).involveDancer(any(DancerInvolvement.class), any(ZonedDateTime.class));
    verify(involver, times(1)).involveOtherDancers(any(Stream.class), eq(time));
  }
}
