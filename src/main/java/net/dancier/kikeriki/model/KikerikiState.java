package net.dancier.kikeriki.state;

import net.dancier.kikeriki.messages.MessageChat;
import net.dancier.kikeriki.messages.MessageLogin;
import net.dancier.kikeriki.messages.MessageMailSent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;


public class KikerikiState
{
  private final Map<UUID, DancerState> state = new HashMap<>();


  DancerState getDancerState(UUID dancerId)
  {
    DancerState dancerState = state.get(dancerId);
    if (dancerState == null)
    {
      dancerState = new DancerState(dancerId);
      state.put(dancerId, dancerState);
    }
    return dancerState;
  }

  public DancerState handle(MessageLogin message)
  {
    UUID dancerId = message.getDancerId();
    DancerState dancerState = getDancerState(dancerId);
    dancerState.setLastLogin(message.getTime());
    return dancerState;
  }

  public DancerState handle(MessageChat message)
  {
    UUID dancerId = message.getDancerId();
    DancerState dancerState = getDancerState(dancerId);
    switch (message.getStatus())
    {
      case NEW:
        dancerState.addUnreadChatMessage(message.getMessageId());
        break;
      case READ:
        dancerState.markChatMessagesAsSeen(message.getTime());
        break;
    }
    return dancerState;
  }

  public DancerState handle(MessageMailSent message)
  {
    UUID dancerId = message.getDancerId();
    DancerState dancerState = getDancerState(dancerId);
    dancerState.setLastMailSent(message.getTime());
    return dancerState;
  }

  public Stream<DancerState> getDancerState()
  {
    return state.values().stream();
  }
}
