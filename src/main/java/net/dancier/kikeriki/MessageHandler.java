package net.dancier.kikeriki;

import net.dancier.kikeriki.messages.*;


public interface MessageHandler
{
  void handle(String key, MessageFoo foo);
  void handle(String key, MessageBar bar);
  void handle(String key, MessageLogin login);
  void handle(String key, MessageChat chat);
  void handle(String key, MessageMailSent mailSent);
}
