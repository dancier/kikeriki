package net.dancier.kikeriki.involvement;

import lombok.extern.slf4j.Slf4j;
import net.dancier.kikeriki.model.DancerState;
import net.dancier.kikeriki.model.InvolvementStrategy;


@Slf4j
public class DummyInvolvementStrategy implements InvolvementStrategy
{
  @Override
  public void involveDancer(DancerState dancerState)
  {
    log.warn("Some fancy dancer-involvment is still not happening -- until YOU implement it here!");
  }
}
