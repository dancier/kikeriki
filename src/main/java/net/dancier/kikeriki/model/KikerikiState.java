package net.dancier.kikeriki.model;

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

  public DancerState handle(MessageLogin messageLogin)
  {
    UUID dancerId = messageLogin.getDancerId();
    DancerState dancerState = getDancerState(dancerId);
    dancerState.setLastLogin(messageLogin.getTime());
    return dancerState;
  }

  public DancerState handle(MessageChat messageChat)
  {
    UUID dancerId = messageChat.getDancerId();
    DancerState dancerState = getDancerState(dancerId);
    switch (messageChat.getStatus())
    {
      case NEW:
        dancerState.addUnreadChatMessage(messageChat.getMessageId());
        break;
      case READ:
        dancerState.markChatMessagesAsSeen(messageChat.getTime());
        break;
    }
    return dancerState;
  }

  public DancerState handle(MessageMailSent messageMailSent)
  {
    UUID dancerId = messageMailSent.getDancerId();
    DancerState dancerState = getDancerState(dancerId);
    dancerState.setLastMailSent(messageMailSent.getTime());
    return dancerState;
  }

  public Stream<DancerState> getDancerState()
  {
    return state.values().stream();
  }
}
