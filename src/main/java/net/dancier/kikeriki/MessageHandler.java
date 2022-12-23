package net.dancier.kikeriki;

import net.dancier.kikeriki.messages.MessageBar;
import net.dancier.kikeriki.messages.MessageFoo;


public interface MessageHandler
{
  void handleFoo(String key, MessageFoo foo);
  void handleBar(String key, MessageBar bar);
}
