package net.dancier.kikeriki.kafka;

import lombok.extern.slf4j.Slf4j;
import net.dancier.kikeriki.messages.*;
import net.dancier.kikeriki.model.DancerState;
import net.dancier.kikeriki.model.KikerikiService;
import net.dancier.kikeriki.model.KikerikiState;
import net.dancier.kikeriki.model.KikerikiStateFactory;

import java.util.Arrays;
import java.util.stream.Stream;


@Slf4j
public class InvolveDancersMessageHandler implements MessageHandler
{
  private final KikerikiStateFactory kikerikiStateFactory;
  private final KikerikiState[] kikerikiState;
  private final long[] endOffsets;
  private final KikerikiService kikerikiService;


  public InvolveDancersMessageHandler(
    KikerikiStateFactory kikerikiStateFactory,
    int numPartitions,
    KikerikiService kikerikiService)
  {
    this.kikerikiStateFactory = kikerikiStateFactory;
    this.kikerikiState = new KikerikiState[numPartitions];
    this.endOffsets = new long[numPartitions];
    this.kikerikiService = kikerikiService;
  }


  @Override
  public void addPartition(int partition, long endOffset)
  {
    kikerikiState[partition] = kikerikiStateFactory.createKikerikiState();
    endOffsets[partition] = endOffset;
  }

  @Override
  public void removePartition(int partition)
  {
    kikerikiState[partition] = null;
    endOffsets[partition] = -1;
  }

  @Override
  public void handle(int partition, long offset, Message message)
  {
    log.info("handling partition={}, message={}", partition, message);
    switch (message.getType())
    {
      case LOGIN:
        handle(partition, offset, (MessageLogin)message);
        break;
      case CHAT:
        handle(partition, offset, (MessageChat)message);
        break;
      case MAIL_SENT:
        handle(partition, offset, (MessageMailSent)message);
        break;
      default:
        throw new RuntimeException("Received message of unknown type: " + message);
    }
  }

  private void handle(int partition, long offset, MessageLogin messageLogin)
  {
    DancerState dancerState = kikerikiState[partition].handle(messageLogin);
    if (endOffsets[partition] <= offset)
    {
      kikerikiService.involveDancer(dancerState, messageLogin.getTime());
      kikerikiService.involveOtherDancers(getDancerState(partition), messageLogin.getTime());
    }
  }

  private void handle(int partition, long offset, MessageChat messageChat)
  {
    DancerState dancerState = kikerikiState[partition].handle(messageChat);
    if (endOffsets[partition] <= offset)
    {
      kikerikiService.involveDancer(dancerState, messageChat.getTime());
      kikerikiService.involveOtherDancers(getDancerState(partition), messageChat.getTime());
    }
  }

  private void handle(int partition, long offset, MessageMailSent messageMailSent)
  {
    kikerikiState[partition].handle(messageMailSent);
    if (endOffsets[partition] <= offset)
    {
      kikerikiService.involveOtherDancers(getDancerState(partition), messageMailSent.getTime());
    }
  }

  public Stream<DancerState> getDancerState(int partition)
  {
    return kikerikiState[partition].getDancerState();
  }

  public Stream<DancerState> getDancerState()
  {
    return Arrays
      .stream(kikerikiState)
      .filter(kikerikiState -> kikerikiState != null)
      .flatMap(kikerikiState -> kikerikiState.getDancerState());
  }
}
