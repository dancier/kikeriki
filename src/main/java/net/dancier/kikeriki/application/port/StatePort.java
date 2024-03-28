package net.dancier.kikeriki.application.port;

import net.dancier.kikeriki.application.domain.model.state.State;

import java.util.UUID;

public interface StatePort {

  State get(String dancerId);

  void save(StateDto stateDto, String dancer_id);

}
