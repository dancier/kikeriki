package net.dancier.kikeriki;

import lombok.extern.slf4j.Slf4j;
import net.dancier.kikeriki.messages.MessageBar;
import net.dancier.kikeriki.messages.MessageFoo;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DumpingMessageHandler implements MessageHandler {
  @Override
  public void handleFoo(String key, MessageFoo foo) {
    log.info("handling foo -> key: %s, message: %s", key, foo);
  }

  @Override
  public void handleBar(String key, MessageBar bar) {
    log.info("handling bar -> key: %s, message: %s", key, bar);
  }
}
