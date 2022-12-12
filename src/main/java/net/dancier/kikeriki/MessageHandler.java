package net.dancier.kikeriki;

import net.dancier.kikeriki.messages.*;


public interface MessageHandler
{
  void handle(String key, Message message);
}
