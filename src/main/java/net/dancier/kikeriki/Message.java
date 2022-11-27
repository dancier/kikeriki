package net.dancier.kikeriki;


public abstract class Message
{
  public enum Type { FOO, BAR }

  public abstract Type getType();
}
