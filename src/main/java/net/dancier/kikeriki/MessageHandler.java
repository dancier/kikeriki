package net.dancier.kikeriki;

public interface MessageHandler
{
  void handleFoo(String key, MessageFoo foo);
  void handleBar(String key, MessageBar bar);
}
