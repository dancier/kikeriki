package net.dancier.kikeriki;

import net.dancier.kikeriki.messages.MessageChat;
import net.dancier.kikeriki.messages.MessageLogin;
import net.dancier.kikeriki.messages.MessageMailSent;
import net.dancier.kikeriki.state.DancerState;
import net.dancier.kikeriki.state.KikerikiState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class InvolveDancersMessageHandlerTest
{
  @Test
  @DisplayName("Receiving an event for a not assigned partition triggers an exception")
  public void testReceivingEventForUnassignedPartitionTriggersException(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver)
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

    // When & Then
    assertThatException().isThrownBy(() -> handler.handle(0, 5l, message));
  }

  @Test
  @DisplayName("Receiving an event for an assigned partition does not trigger an exception")
  public void testReceivingEventForAssignedPartitionDoesNotTriggerException(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver)
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

    // When & Then
    assertThatNoException().isThrownBy(() -> handler.handle(0, 5l, message));
  }

  @Test
  @DisplayName("Receiving an event for a revoked partition triggers an exception")
  public void testReceivingEventForRevokedPartitionTriggersException(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver)
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
    handler.removePartition(0);

    // When & Then
    assertThatException().isThrownBy(() -> handler.handle(0, 5l, message));
  }

  @Test
  @DisplayName("Handling of an outdated login-message")
  public void testHandleLoginMessageWithInvolvementDisabled(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver,
    @Mock DancerState dancerState)
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
    when(state.handle(any(MessageLogin.class))).thenReturn(dancerState);

    // When
    handler.handle(0, 5l, message);

    // Then
    verify(state, times(1)).handle(message);
    verify(involver, never()).involveDancer(any(DancerState.class), any(ZonedDateTime.class));
    verify(involver, never()).involveOtherDancers(any(Stream.class), any(ZonedDateTime.class));
  }

  @Test
  @DisplayName("Handling of a current login-message")
  public void testHandleLoginMessageWithInvolvementEnabled(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver,
    @Mock DancerState dancerState)
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
    when(state.handle(any(MessageLogin.class))).thenReturn(dancerState);

    // When
    handler.handle(0, 6l, message);

    // Then
    verify(state, times(1)).handle(message);
    verify(involver, times(1)).involveDancer(dancerState, time);
    verify(involver, times(1)).involveOtherDancers(any(Stream.class), eq(time));
  }

  @Test
  @DisplayName("Handling of a outdated chat-message")
  public void testHandleChatMessageWithInvolvementDisabled(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver,
    @Mock DancerState dancerState)
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
    when(state.handle(any(MessageChat.class))).thenReturn(dancerState);

    // When
    handler.handle(0, 5l, message);

    // Then
    verify(state, times(1)).handle(message);
    verify(involver, never()).involveDancer(any(DancerState.class), any(ZonedDateTime.class));
    verify(involver, never()).involveOtherDancers(any(Stream.class), any(ZonedDateTime.class));
  }

  @Test
  @DisplayName("Handling of a current chat-message")
  public void testHandleChatMessageWithInvolvementEnabled(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver,
    @Mock DancerState dancerState)
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
    when(state.handle(any(MessageChat.class))).thenReturn(dancerState);

    // When
    handler.handle(0, 6l, message);

    // Then
    verify(state, times(1)).handle(message);
    verify(involver, times(1)).involveDancer(dancerState, time);
    verify(involver, times(1)).involveOtherDancers(any(Stream.class), eq(time));
  }

  @Test
  @DisplayName("Handling of a outdated mail-sent-message")
  public void testHandleMailSentMessageWithInvolvementDisabled(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver,
    @Mock DancerState dancerState)
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
    when(state.handle(any(MessageMailSent.class))).thenReturn(dancerState);

    // When
    handler.handle(0, 5l, message);

    // Then
    verify(state, times(1)).handle(message);
    verify(involver, never()).involveDancer(any(DancerState.class), any(ZonedDateTime.class));
    verify(involver, never()).involveOtherDancers(any(Stream.class), any(ZonedDateTime.class));
  }

  @Test
  @DisplayName("Handling of a current mail-sent-message")
  public void testHandleMailSentMessageWithInvolvementEnabled(
    @Mock KikerikiState state,
    @Mock DancerInvolver involver,
    @Mock DancerState dancerState)
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
    when(state.handle(any(MessageMailSent.class))).thenReturn(dancerState);

    // When
    handler.handle(0, 6l, message);

    // Then
    verify(state, times(1)).handle(message);
    verify(involver, never()).involveDancer(any(DancerState.class), any(ZonedDateTime.class));
    verify(involver, times(1)).involveOtherDancers(any(Stream.class), eq(time));
  }
}
