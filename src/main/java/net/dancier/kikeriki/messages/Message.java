package net.dancier.kikeriki.messages;


public abstract class Message
{
  public enum Type { FOO, BAR, CHAT }

  public abstract Type getType();
}
