package net.dancier.kikeriki.state;

import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.messages.MessageChat;
import net.dancier.kikeriki.messages.MessageLogin;
import net.dancier.kikeriki.messages.MessageMailSent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

  public void handle(MessageLogin message)
  {
    UUID dancerId = message.getDancerId();
    DancerInvolvement dancerInvolvement = getDancerInvolvement(dancerId);
    dancerInvolvement.setLastLogin(message.getTime());
  }

  public void handle(MessageChat message)
  {
    UUID dancerId = message.getDancerId();
    DancerInvolvement dancerInvolvement = getDancerInvolvement(dancerId);
    switch (message.getStatus())
    {
      case NEW:
        dancerInvolvement.addUnreadMessage(message.getMessageId());
        break;
      case READ:
        dancerInvolvement.setLastMessageRead(message.getTime());
        dancerInvolvement.markMessageAsRead(message.getMessageId());
        break;
    }
  }

  public void handle(MessageMailSent message)
  {
    UUID dancerId = message.getDancerId();
    DancerInvolvement dancerInvolvement = getDancerInvolvement(dancerId);
    dancerInvolvement.setLastMailSent(message.getTime());
  }
}
