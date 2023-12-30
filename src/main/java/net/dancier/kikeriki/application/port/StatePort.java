package net.dancier.kikeriki.application.port;

import net.dancier.kikeriki.application.domain.model.state.State;

import java.util.UUID;

public interface StatePort {

  State get(UUID dancerId);

  void save(State state);

}
