package net.dancier.kikeriki.kafka;

import net.dancier.kikeriki.messages.MessageChat;
import net.dancier.kikeriki.messages.MessageLogin;
import net.dancier.kikeriki.messages.MessageMailSent;
import net.dancier.kikeriki.model.KikerikiService;
import net.dancier.kikeriki.model.KikerikiState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.UUID;

import static net.dancier.kikeriki.messages.MessageChat.ChatMessageStatus.NEW;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class InvolveDancersMessageHandlerIT
{
  InvolveDancersMessageHandler involveDancersMessageHandler;
  KikerikiService kikerikiService;

  Clock clock;
  UUID dancerId;
  UUID messageId;

  @BeforeEach
  public void setUp()
  {
    kikerikiService = new KikerikiService(
      Duration.ofMinutes(1),
      Duration.ofMinutes(10),
      Duration.ofHours(1),
      dancerState -> {});

    involveDancersMessageHandler = new InvolveDancersMessageHandler(
      () -> new KikerikiState(),
      1,
      kikerikiService);

    ZoneId zoneId = ZoneId.of("US/Eastern");
    ZonedDateTime zonedDateTime = ZonedDateTime.of(2004,7, 25,17,18,0,0,zoneId);
    clock = Clock.fixed(zonedDateTime.toInstant(), zoneId);

    dancerId = UUID.fromString("ED7BA470-8E54-465E-825C-99712043E01C");
    messageId = UUID.fromString("DF8395B5-A4BA-450b-A77C-A9A47762C520");
  }

  @Test
  @DisplayName("Handline a message for a not owned partition throws a IllegalStateException")
  public void testIllegalStateExceptionIfPartitionNotOwned()
  {
    assertThatThrownBy(() -> involveDancersMessageHandler.handle(0,0, messageLogin()))
      .as("Handling MessageLogin")
      .isInstanceOf(IllegalStateException.class);
    assertThatThrownBy(() -> involveDancersMessageHandler.handle(0,0, messageChat()))
      .as("Handling MessageChat")
      .isInstanceOf(IllegalStateException.class);
    assertThatThrownBy(() -> involveDancersMessageHandler.handle(0,0, messageMailSent()))
      .as("Handling MessageMailSent")
      .isInstanceOf(IllegalStateException.class);
  }


  MessageLogin messageLogin()
  {
    MessageLogin messageLogin = new MessageLogin();
    messageLogin.setTime(ZonedDateTime.now(clock));
    messageLogin.setDancerId(dancerId);
    return messageLogin;
  }

  MessageChat messageChat()
  {
    MessageChat messageChat = new MessageChat();
    messageChat.setTime(ZonedDateTime.now(clock));
    messageChat.setDancerId(dancerId);
    messageChat.setMessageId(messageId);
    messageChat.setStatus(NEW);
    return messageChat;
  }

  MessageMailSent messageMailSent()
  {
    MessageMailSent messageMailSent = new MessageMailSent();
    messageMailSent.setTime(ZonedDateTime.now(clock));
    messageMailSent.setDancerId(dancerId);
    return messageMailSent;
  }
}
