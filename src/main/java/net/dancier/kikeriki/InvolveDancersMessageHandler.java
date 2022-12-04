package net.dancier.kikeriki;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dancier.kikeriki.messages.*;
import net.dancier.kikeriki.state.DancerInvolvement;
import net.dancier.kikeriki.state.KikerikiState;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;


@Slf4j
@RequiredArgsConstructor
public class InvolveDancersMessageHandler implements MessageHandler
{
  private final KikerikiState state;
  private final Duration involveDancerAfter;
  private final Duration involvementCheckInterval;

  private ZonedDateTime lastGeneralInvolvement = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("Europe/Berlin"));

  @Getter
  @Setter
  private boolean involvementEnabled = true;

  @Override
  public void handle(String key, Message message)
  {
    switch (message.getType())
    {
      case FOO:
        handle(key, (MessageFoo)message);
        break;
      case BAR:
        handle(key, (MessageBar)message);
        break;
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

  void handle(String key, MessageFoo foo)
  {
    log.info("handling foo -> key: %s, message: %s", key, foo);
  }

  void handle(String key, MessageBar bar)
  {
    log.info("handling bar -> key: %s, message: %s", key, bar);
  }

  void handle(String key, MessageLogin message)
  {
    log.info("handling key=%s, message=%s", key, message);
    DancerInvolvement dancerInvolvement = state.handle(message);
    ZonedDateTime lastInvolvement = dancerInvolvement.getLastInvolvement();
    involveDancer(dancerInvolvement, lastInvolvement, message.getTime());
    involveOtherDancers(message.getTime());
  }

  void handle(String key, MessageChat message)
  {
    log.info("handling key=%s, message=%s", key, message);
    DancerInvolvement dancerInvolvement = state.handle(message);
    ZonedDateTime lastInvolvement = dancerInvolvement.getLastInvolvement();
    involveDancer(dancerInvolvement, lastInvolvement, message.getTime());
    involveOtherDancers(message.getTime());
  }

  void handle(String key, MessageMailSent message)
  {
    log.info("handling key=%s, message=%s", key, message);
    state.handle(message);
    involveOtherDancers(message.getTime());
  }

  void involveDancer(
    DancerInvolvement dancerInvolvement,
    ZonedDateTime lastInvolvement,
    ZonedDateTime now)
  {
    if (!involvementEnabled)
      return;

    if (lastInvolvement.plus(involveDancerAfter).isBefore(now))
    {
      log.info(
        "involving dancer %s (last involvement={}, now={})",
        dancerInvolvement.getDancerId(),
        lastInvolvement,
        now);
    }
  }

  void involveOtherDancers(ZonedDateTime now)
  {
    if (!involvementEnabled)
      return;

    if (lastGeneralInvolvement.plus(involvementCheckInterval).isAfter(now))
      return;

    lastGeneralInvolvement = now;
    state
      .getDancerInvolvements()
      .filter(dancerInvolvement -> dancerInvolvement.getLastInvolvement().plus(involveDancerAfter).isBefore(now))
      .forEach(dancerInvolvement -> sendMail(dancerInvolvement, now));
  }

  void sendMail(DancerInvolvement involvement, ZonedDateTime now)
  {
    // TODO: Send a Mail and emmit a message of type MessageMailSent
  }
}
