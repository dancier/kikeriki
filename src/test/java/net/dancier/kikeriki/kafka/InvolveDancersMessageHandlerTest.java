package net.dancier.kikeriki.kafka;

import net.dancier.kikeriki.messages.MessageChat;
import net.dancier.kikeriki.messages.MessageLogin;
import net.dancier.kikeriki.messages.MessageMailSent;
import net.dancier.kikeriki.model.DancerState;
import net.dancier.kikeriki.model.KikerikiService;
import net.dancier.kikeriki.model.KikerikiState;
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
    @Mock KikerikiState kikerikiState,
    @Mock KikerikiService kikerikiService)
  {
    // Given
    InvolveDancersMessageHandler involveDancersMessageHandler =
      new InvolveDancersMessageHandler(
        () -> kikerikiState,
        1,
        kikerikiService);
    MessageLogin messageLogin = new MessageLogin();
    ZonedDateTime time = ZonedDateTime.now();
    messageLogin.setTime(time);

    // When & Then
    assertThatException().isThrownBy(() -> involveDancersMessageHandler.handle(0, 5l, messageLogin));
  }

  @Test
  @DisplayName("Receiving an event for an assigned partition does not trigger an exception")
  public void testReceivingEventForAssignedPartitionDoesNotTriggerException(
    @Mock KikerikiState kikerikiState,
    @Mock KikerikiService kikerikiService)
  {
    // Given
    InvolveDancersMessageHandler involveDancersMessageHandler =
      new InvolveDancersMessageHandler(
        () -> kikerikiState,
        1,
        kikerikiService);
    MessageLogin messageLogin = new MessageLogin();
    ZonedDateTime time = ZonedDateTime.now();
    messageLogin.setTime(time);
    involveDancersMessageHandler.addPartition(0, 6);

    // When & Then
    assertThatNoException().isThrownBy(() -> involveDancersMessageHandler.handle(0, 5l, messageLogin));
  }

  @Test
  @DisplayName("Receiving an event for a revoked partition triggers an exception")
  public void testReceivingEventForRevokedPartitionTriggersException(
    @Mock KikerikiState kikerikiState,
    @Mock KikerikiService kikerikiService)
  {
    // Given
    InvolveDancersMessageHandler involveDancersMessageHandler =
      new InvolveDancersMessageHandler(
        () -> kikerikiState,
        1,
        kikerikiService);
    MessageLogin messageLogin = new MessageLogin();
    ZonedDateTime time = ZonedDateTime.now();
    messageLogin.setTime(time);
    involveDancersMessageHandler.addPartition(0, 6);
    involveDancersMessageHandler.removePartition(0);

    // When & Then
    assertThatException().isThrownBy(() -> involveDancersMessageHandler.handle(0, 5l, messageLogin));
  }

  @Test
  @DisplayName("Handling of an outdated login-message")
  public void testHandleLoginMessageWithInvolvementDisabled(
    @Mock KikerikiState kikerikiState,
    @Mock KikerikiService kikerikiService,
    @Mock DancerState dancerState)
  {
    // Given
    InvolveDancersMessageHandler involveDancersMessageHandler =
      new InvolveDancersMessageHandler(
        () -> kikerikiState,
        1,
        kikerikiService);
    MessageLogin messageLogin = new MessageLogin();
    ZonedDateTime time = ZonedDateTime.now();
    messageLogin.setTime(time);
    involveDancersMessageHandler.addPartition(0, 6);
    when(kikerikiState.handle(any(MessageLogin.class))).thenReturn(dancerState);

    // When
    involveDancersMessageHandler.handle(0, 5l, messageLogin);

    // Then
    verify(kikerikiState, times(1)).handle(messageLogin);
    verify(kikerikiService, never()).involveDancer(any(DancerState.class), any(ZonedDateTime.class));
    verify(kikerikiService, never()).involveOtherDancers(any(Stream.class), any(ZonedDateTime.class));
  }

  @Test
  @DisplayName("Handling of a current login-message")
  public void testHandleLoginMessageWithInvolvementEnabled(
    @Mock KikerikiState kikerikiState,
    @Mock KikerikiService kikerikiService,
    @Mock DancerState dancerState)
  {
    // Given
    InvolveDancersMessageHandler involveDancersMessageHandler =
      new InvolveDancersMessageHandler(
        () -> kikerikiState,
        1,
        kikerikiService);
    MessageLogin messageLogin = new MessageLogin();
    ZonedDateTime time = ZonedDateTime.now();
    messageLogin.setTime(time);
    involveDancersMessageHandler.addPartition(0, 6);
    when(kikerikiState.handle(any(MessageLogin.class))).thenReturn(dancerState);

    // When
    involveDancersMessageHandler.handle(0, 6l, messageLogin);

    // Then
    verify(kikerikiState, times(1)).handle(messageLogin);
    verify(kikerikiService, times(1)).involveDancer(dancerState, time);
    verify(kikerikiService, times(1)).involveOtherDancers(any(Stream.class), eq(time));
  }

  @Test
  @DisplayName("Handling of a outdated chat-message")
  public void testHandleChatMessageWithInvolvementDisabled(
    @Mock KikerikiState kikerikiState,
    @Mock KikerikiService kikerikiService,
    @Mock DancerState dancerState)
  {
    // Given
    InvolveDancersMessageHandler involveDancersMessageHandler =
      new InvolveDancersMessageHandler(
        () -> kikerikiState,
        1,
        kikerikiService);
    MessageChat messageChat = new MessageChat();
    ZonedDateTime time = ZonedDateTime.now();
    messageChat.setTime(time);
    involveDancersMessageHandler.addPartition(0, 6);
    when(kikerikiState.handle(any(MessageChat.class))).thenReturn(dancerState);

    // When
    involveDancersMessageHandler.handle(0, 5l, messageChat);

    // Then
    verify(kikerikiState, times(1)).handle(messageChat);
    verify(kikerikiService, never()).involveDancer(any(DancerState.class), any(ZonedDateTime.class));
    verify(kikerikiService, never()).involveOtherDancers(any(Stream.class), any(ZonedDateTime.class));
  }

  @Test
  @DisplayName("Handling of a current chat-message")
  public void testHandleChatMessageWithInvolvementEnabled(
    @Mock KikerikiState kikerikiState,
    @Mock KikerikiService kikerikiService,
    @Mock DancerState dancerState)
  {
    // Given
    InvolveDancersMessageHandler involveDancersMessageHandler =
      new InvolveDancersMessageHandler(
        () -> kikerikiState,
        1,
        kikerikiService);
    MessageChat messageChat = new MessageChat();
    ZonedDateTime time = ZonedDateTime.now();
    messageChat.setTime(time);
    involveDancersMessageHandler.addPartition(0, 6);
    when(kikerikiState.handle(any(MessageChat.class))).thenReturn(dancerState);

    // When
    involveDancersMessageHandler.handle(0, 6l, messageChat);

    // Then
    verify(kikerikiState, times(1)).handle(messageChat);
    verify(kikerikiService, times(1)).involveDancer(dancerState, time);
    verify(kikerikiService, times(1)).involveOtherDancers(any(Stream.class), eq(time));
  }

  @Test
  @DisplayName("Handling of a outdated mail-sent-message")
  public void testHandleMailSentMessageWithInvolvementDisabled(
    @Mock KikerikiState kikerikiState,
    @Mock KikerikiService kikerikiService,
    @Mock DancerState dancerState)
  {
    // Given
    InvolveDancersMessageHandler involveDancersMessageHandler =
      new InvolveDancersMessageHandler(
        () -> kikerikiState,
        1,
        kikerikiService);
    MessageMailSent messageMailSent = new MessageMailSent();
    ZonedDateTime time = ZonedDateTime.now();
    messageMailSent.setTime(time);
    involveDancersMessageHandler.addPartition(0, 6);
    when(kikerikiState.handle(any(MessageMailSent.class))).thenReturn(dancerState);

    // When
    involveDancersMessageHandler.handle(0, 5l, messageMailSent);

    // Then
    verify(kikerikiState, times(1)).handle(messageMailSent);
    verify(kikerikiService, never()).involveDancer(any(DancerState.class), any(ZonedDateTime.class));
    verify(kikerikiService, never()).involveOtherDancers(any(Stream.class), any(ZonedDateTime.class));
  }

  @Test
  @DisplayName("Handling of a current mail-sent-message")
  public void testHandleMailSentMessageWithInvolvementEnabled(
    @Mock KikerikiState kikerikiState,
    @Mock KikerikiService kikerikiService,
    @Mock DancerState dancerState)
  {
    // Given
    InvolveDancersMessageHandler involveDancersMessageHandler =
      new InvolveDancersMessageHandler(
        () -> kikerikiState,
        1,
        kikerikiService);
    MessageMailSent messageMailSent = new MessageMailSent();
    ZonedDateTime time = ZonedDateTime.now();
    messageMailSent.setTime(time);
    involveDancersMessageHandler.addPartition(0, 6);
    when(kikerikiState.handle(any(MessageMailSent.class))).thenReturn(dancerState);

    // When
    involveDancersMessageHandler.handle(0, 6l, messageMailSent);

    // Then
    verify(kikerikiState, times(1)).handle(messageMailSent);
    verify(kikerikiService, never()).involveDancer(any(DancerState.class), any(ZonedDateTime.class));
    verify(kikerikiService, times(1)).involveOtherDancers(any(Stream.class), eq(time));
  }
}
