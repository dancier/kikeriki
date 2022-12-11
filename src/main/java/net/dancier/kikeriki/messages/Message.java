package net.dancier.kikeriki.messages;


public abstract class Message
{
  public enum Type { CHAT, LOGIN, MAIL_SENT }

  public abstract Type getType();
}
