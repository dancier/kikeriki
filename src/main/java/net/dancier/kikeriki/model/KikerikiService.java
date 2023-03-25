package net.dancier.kikeriki.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Stream;


@Slf4j
@RequiredArgsConstructor
public class KikerikiService
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
    ZonedDateTime streamTimeNow)
  {
    if (dancerState
      .getLastInvolvement()
      .orElse(NEVER)
      .plus(involveDancerAfter).isBefore(streamTimeNow))
    {
      log.info(
        "involving dancer {} (last involvement={}, now={})",
        dancerState.getDancerId(),
        dancerState.getLastInvolvement(),
        streamTimeNow);
    }
  }

  public void involveOtherDancers(
    Stream<DancerState> dancerStateStream,
    ZonedDateTime streamTimeNow)
  {
    if (lastGeneralInvolvement
      .orElse(NEVER)
      .plus(involvementCheckInterval).isAfter(streamTimeNow))
    {
      return;
    }

    lastGeneralInvolvement = Optional.of(streamTimeNow);
    dancerStateStream
      .filter(dancerState -> dancerState
        .getLastInvolvement()
        .orElse(NEVER)
        .plus(involveDancerAfter)
        .isBefore(streamTimeNow))
      .forEach(dancerState -> sendMail(dancerState, streamTimeNow));
  }

  void sendMail(
    DancerState dancerState,
    ZonedDateTime streamTimeNow)
  {
    if (dancerState
      .getLastMailSent()
      .orElse(NEVER)
      .plus(reinvolvementInterval)
      .isAfter(streamTimeNow))
    {
      // Do not send involvement-mails more frequent than defined in reinvolvementInterval,
      // if the user does not react to it
      return;
    }

    // TODO: Send a Mail and emmit a message of type MessageMailSent
    log.warn("A mails should be send here!");
  }
}
