package net.dancier.kikeriki;

import net.dancier.kikeriki.messages.*;


public interface MessageHandler
{
  void handle(int partition, long offset, Message message);

  void addPartition(int partition, long endOffset);
  void removePartition(int partition);
}
