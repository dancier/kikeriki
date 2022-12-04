package net.dancier.kikeriki;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dancier.kikeriki.messages.*;
import net.dancier.kikeriki.state.KikerikiState;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class InvolveDancersMessageHandler implements MessageHandler
{
  private final KikerikiState state;

  @Override
  public void handle(String key, MessageFoo foo)
  {
    log.info("handling foo -> key: %s, message: %s", key, foo);
  }

  @Override
  public void handle(String key, MessageBar bar)
  {
    log.info("handling bar -> key: %s, message: %s", key, bar);
  }

  public void handle(String key, MessageLogin login)
  {
    log.info("handling login -> key: %s, message: %s", key, login);
    state.handle(login);
  }

  public void handle(String key, MessageChat chat)
  {
    log.info("handling chat -> key: %s, message: %s", key, chat);
    state.handle(chat);
  }

  public void handle(String key, MessageMailSent mailSent)
  {
    log.info("handling mailSent -> key: %s, message: %s", key, mailSent);
    state.handle(mailSent);
  }
}
