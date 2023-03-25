package net.dancier.kikeriki;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dancier.kikeriki.state.DancerInvolvement;

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
    DancerInvolvement dancerInvolvement,
    ZonedDateTime now)
  {
    if (dancerInvolvement
      .getLastInvolvement()
      .orElse(NEVER)
      .plus(involveDancerAfter).isBefore(now))
    {
      log.info(
        "involving dancer {} (last involvement={}, now={})",
        dancerInvolvement.getDancerId(),
        dancerInvolvement.getLastInvolvement(),
        now);
    }
  }

  public void involveOtherDancers(Stream<DancerInvolvement> involvements, ZonedDateTime now)
  {
    if (lastGeneralInvolvement
      .orElse(NEVER)
      .plus(involvementCheckInterval).isAfter(now))
    {
      return;
    }

    lastGeneralInvolvement = Optional.of(now);
    involvements
      .filter(dancerInvolvement -> dancerInvolvement
        .getLastInvolvement()
        .orElse(NEVER)
        .plus(involveDancerAfter)
        .isBefore(now))
      .forEach(dancerInvolvement -> sendMail(dancerInvolvement, now));
  }

  void sendMail(DancerInvolvement dancerInvolvement, ZonedDateTime now)
  {
    if (dancerInvolvement
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
