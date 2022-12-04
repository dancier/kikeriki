package net.dancier.kikeriki.messages;


public abstract class Message
{
  public enum Type { FOO, BAR, CHAT, LOGIN, MAIL_SENT }

  public abstract Type getType();
}
