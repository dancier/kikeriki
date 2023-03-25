package net.dancier.kikeriki.kafka;

import lombok.extern.slf4j.Slf4j;
import net.dancier.kikeriki.DancerInvolver;
import net.dancier.kikeriki.messages.*;
import net.dancier.kikeriki.state.DancerState;
import net.dancier.kikeriki.state.KikerikiState;
import net.dancier.kikeriki.state.KikerikiStateFactory;

import java.util.Arrays;
import java.util.stream.Stream;


@Slf4j
public class InvolveDancersMessageHandler implements MessageHandler
{
  private final KikerikiStateFactory stateFactory;
  private final KikerikiState[] state;
  private final long[] endOffsets;
  private final DancerInvolver involver;


  public InvolveDancersMessageHandler(
    KikerikiStateFactory stateFactory,
    int numPartitions,
    DancerInvolver involver)
  {
    this.stateFactory = stateFactory;
    this.state = new KikerikiState[numPartitions];
    this.endOffsets = new long[numPartitions];
    this.involver = involver;
  }


  @Override
  public void addPartition(int partition, long endOffset)
  {
    state[partition] = stateFactory.createKikerikiState();
    endOffsets[partition] = endOffset;
  }

  @Override
  public void removePartition(int partition)
  {
    state[partition] = null;
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
    DancerState dancerState = state[partition].handle(messageLogin);
    if (endOffsets[partition] <= offset)
    {
      involver.involveDancer(dancerState, messageLogin.getTime());
      involver.involveOtherDancers(getDancerState(partition), messageLogin.getTime());
    }
  }

  private void handle(int partition, long offset, MessageChat messageChat)
  {
    DancerState dancerState = state[partition].handle(messageChat);
    if (endOffsets[partition] <= offset)
    {
      involver.involveDancer(dancerState, messageChat.getTime());
      involver.involveOtherDancers(getDancerState(partition), messageChat.getTime());
    }
  }

  private void handle(int partition, long offset, MessageMailSent messageMailSent)
  {
    state[partition].handle(messageMailSent);
    if (endOffsets[partition] <= offset)
    {
      involver.involveOtherDancers(getDancerState(partition), messageMailSent.getTime());
    }
  }

  public Stream<DancerState> getDancerState(int partition)
  {
    return state[partition].getDancerState();
  }

  public Stream<DancerState> getDancerState()
  {
    return Arrays
      .stream(state)
      .filter(kikerikiState -> kikerikiState != null)
      .flatMap(kikerikiState -> kikerikiState.getDancerState());
  }
}
