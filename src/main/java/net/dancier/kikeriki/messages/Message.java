package net.dancier.kikeriki.messages;


public abstract class Message
{
  public enum Type { FOO, BAR }

  public abstract Type getType();
}
