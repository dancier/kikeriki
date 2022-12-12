package net.dancier.kikeriki.state;

import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.messages.MessageChat;
import net.dancier.kikeriki.messages.MessageLogin;
import net.dancier.kikeriki.messages.MessageMailSent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;


@Component
@RequiredArgsConstructor
public class KikerikiState
{
  private final Map<UUID, DancerInvolvement> state = new HashMap<>();


  DancerInvolvement getDancerInvolvement(UUID dancerId)
  {
    DancerInvolvement involvement = state.get(dancerId);
    if (involvement == null)
    {
      involvement = new DancerInvolvement(dancerId);
      state.put(dancerId, involvement);
    }
    return involvement;
  }

  public DancerInvolvement handle(MessageLogin message)
  {
    UUID dancerId = message.getDancerId();
    DancerInvolvement dancerInvolvement = getDancerInvolvement(dancerId);
    dancerInvolvement.setLastLogin(message.getTime());
    return dancerInvolvement;
  }

  public DancerInvolvement handle(MessageChat message)
  {
    UUID dancerId = message.getDancerId();
    DancerInvolvement dancerInvolvement = getDancerInvolvement(dancerId);
    switch (message.getStatus())
    {
      case NEW:
        dancerInvolvement.addUnreadChatMessage(message.getMessageId());
        break;
      case READ:
        dancerInvolvement.markChatMessagesAsSeen(message.getTime());
        break;
    }
    return dancerInvolvement;
  }

  public DancerInvolvement handle(MessageMailSent message)
  {
    UUID dancerId = message.getDancerId();
    DancerInvolvement dancerInvolvement = getDancerInvolvement(dancerId);
    dancerInvolvement.setLastMailSent(message.getTime());
    return dancerInvolvement;
  }

  public Stream<DancerInvolvement> getDancerInvolvements()
  {
    return state.values().stream();
  }
}
