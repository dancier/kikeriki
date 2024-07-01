package net.dancier.kikeriki.adapter.out.persistence.state;

import net.dancier.kikeriki.application.domain.model.state.State;


public class StateMapper {

  public static State of(StateJpaEntity stateJpaEntity) {
    State state = new State();
    if (stateJpaEntity
          .getData()
          .getOptSendlastMessage()
          .isPresent()) {
      state.setLastMailMessage(stateJpaEntity.getData().getOptSendlastMessage().get());
    }
    stateJpaEntity.getData().getUnreadChatMessages().stream().forEach(
      ucm -> {
        state.addUnreadChatMessage(ucm);
      }
    );
    return state;
  }
}
