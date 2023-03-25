package net.dancier.kikeriki;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dancier.kikeriki.state.DancerState;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Stream;


@Slf4j
@RequiredArgsConstructor
public class DancerInvolver
{
  private final static ZonedDateTime NEVER =
    ZonedDateTime.ofInstant(
      Instant.EPOCH,
      ZoneId.of("Europe/Berlin"));

  private final Duration involveDancerAfter;
  private final Duration involvementCheckInterval;
  private final Duration reinvolvementInterval;

  private Optional<ZonedDateTime> lastGeneralInvolvement = Optional.empty();


  public void involveDancer(
    DancerState dancerState,
    ZonedDateTime now)
  {
    if (dancerState
      .getLastInvolvement()
      .orElse(NEVER)
      .plus(involveDancerAfter).isBefore(now))
    {
      log.info(
        "involving dancer {} (last involvement={}, now={})",
        dancerState.getDancerId(),
        dancerState.getLastInvolvement(),
        now);
    }
  }

  public void involveOtherDancers(Stream<DancerState> dancerStateStream, ZonedDateTime now)
  {
    if (lastGeneralInvolvement
      .orElse(NEVER)
      .plus(involvementCheckInterval).isAfter(now))
    {
      return;
    }

    lastGeneralInvolvement = Optional.of(now);
    dancerStateStream
      .filter(dancerState -> dancerState
        .getLastInvolvement()
        .orElse(NEVER)
        .plus(involveDancerAfter)
        .isBefore(now))
      .forEach(dancerState -> sendMail(dancerState, now));
  }

  void sendMail(DancerState dancerState, ZonedDateTime now)
  {
    if (dancerState
      .getLastMailSent()
      .orElse(NEVER)
      .plus(reinvolvementInterval)
      .isAfter(now))
    {
      // Do not send involvement-mails more frequent than defined in reinvolvementInterval,
      // if the user does not react to it
      return;
    }

    // TODO: Send a Mail and emmit a message of type MessageMailSent
    log.warn("A mails should be send here!");
  }
}
