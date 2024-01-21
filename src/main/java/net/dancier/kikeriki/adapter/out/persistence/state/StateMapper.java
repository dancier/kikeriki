package net.dancier.kikeriki.adapter.out.persistence.state;

import net.dancier.kikeriki.application.domain.model.state.State;


public class StateMapper {

  public static State of(StateJpaEntity stateJpaEntity) {
    State state = new State();

    return state;
  }
}
