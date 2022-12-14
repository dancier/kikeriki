package net.dancier.kikeriki;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dancier.kikeriki.state.DancerInvolvement;
import net.dancier.kikeriki.state.KikerikiState;

import java.time.Duration;
import java.time.ZonedDateTime;


@Slf4j
@RequiredArgsConstructor
public class DancerInvolver
{
  private final KikerikiState state;
  private final Duration involveDancerAfter;
  private final Duration involvementCheckInterval;
  private final Duration reinvolvementInterval;

  private ZonedDateTime lastGeneralInvolvement = DancerInvolvement.NEVER;


  public void involveDancer(
    DancerInvolvement dancerInvolvement,
    ZonedDateTime now)
  {
    if (dancerInvolvement.getLastInvolvement().plus(involveDancerAfter).isBefore(now))
    {
      log.info(
        "involving dancer %s (last involvement={}, now={})",
        dancerInvolvement.getDancerId(),
        dancerInvolvement.getLastInvolvement(),
        now);
    }
  }

  public void involveOtherDancers(ZonedDateTime now)
  {
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
    if (involvement.getLastMailSent().plus(reinvolvementInterval).isAfter(now))
      // Do not send involvement-mails more frequent than defined in reinvolvementInterval,
      // if the user does not react to it
      return;

    // TODO: Send a Mail and emmit a message of type MessageMailSent
  }
}
