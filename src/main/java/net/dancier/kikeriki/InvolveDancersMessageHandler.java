package net.dancier.kikeriki;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dancier.kikeriki.messages.*;
import net.dancier.kikeriki.state.DancerInvolvement;
import net.dancier.kikeriki.state.KikerikiState;


@Slf4j
@RequiredArgsConstructor
public class InvolveDancersMessageHandler implements MessageHandler
{
  private final KikerikiState state;
  private final DancerInvolver involver;

  @Getter
  @Setter
  private boolean involvementEnabled = true;

  @Override
  public void handle(String key, Message message)
  {
    log.info("handling key={}, message={}", key, message);
    switch (message.getType())
    {
      case LOGIN:
        handle(key, (MessageLogin)message);
        break;
      case CHAT:
        handle(key, (MessageChat)message);
        break;
      case MAIL_SENT:
        handle(key, (MessageMailSent)message);
        break;
      default:
        throw new RuntimeException("Received message of unknown type: " + message);
    }
  }

  void handle(String key, MessageLogin message)
  {
    DancerInvolvement dancerInvolvement = state.handle(message);
    if (involvementEnabled)
    {
      involver.involveDancer(dancerInvolvement, message.getTime());
      involver.involveOtherDancers(message.getTime());
    }
  }

  void handle(String key, MessageChat message)
  {
    DancerInvolvement dancerInvolvement = state.handle(message);
    if (involvementEnabled)
    {
      involver.involveDancer(dancerInvolvement, message.getTime());
      involver.involveOtherDancers(message.getTime());
    }
  }

  void handle(String key, MessageMailSent message)
  {
    state.handle(message);
    if (involvementEnabled)
    {
      involver.involveOtherDancers(message.getTime());
    }
  }
}
