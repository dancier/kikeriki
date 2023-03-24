package net.dancier.kikeriki;

import lombok.extern.slf4j.Slf4j;
import net.dancier.kikeriki.messages.*;
import net.dancier.kikeriki.state.DancerInvolvement;
import net.dancier.kikeriki.state.KikerikiState;
import net.dancier.kikeriki.state.KikerikiStateFactory;

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

  private void handle(int partition, long offset, MessageLogin message)
  {
    DancerInvolvement dancerInvolvement = state[partition].handle(message);
    if (endOffsets[partition] <= offset)
    {
      involver.involveDancer(dancerInvolvement, message.getTime());
      involver.involveOtherDancers(getDancerInvolvements(partition), message.getTime());
    }
  }

  private void handle(int partition, long offset, MessageChat message)
  {
    DancerInvolvement dancerInvolvement = state[partition].handle(message);
    if (endOffsets[partition] <= offset)
    {
      involver.involveDancer(dancerInvolvement, message.getTime());
      involver.involveOtherDancers(getDancerInvolvements(partition), message.getTime());
    }
  }

  private void handle(int partition, long offset, MessageMailSent message)
  {
    state[partition].handle(message);
    if (endOffsets[partition] <= offset)
    {
      involver.involveOtherDancers(getDancerInvolvements(partition), message.getTime());
    }
  }

  Stream<DancerInvolvement> getDancerInvolvements(int partition)
  {
    return state[partition].getDancerInvolvements();
  }
}
