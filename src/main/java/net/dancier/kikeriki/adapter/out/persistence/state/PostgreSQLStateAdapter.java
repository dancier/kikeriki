package net.dancier.kikeriki.adapter.out.persistence.state;

import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.application.domain.model.state.State;
import net.dancier.kikeriki.application.port.StatePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;


@RequiredArgsConstructor
@Component
public class PostgreSQLStateAdapter implements StatePort {

  private final static Logger log = LoggerFactory.getLogger(PostgreSQLStateAdapter.class);

  private final StateJpaRepository stateJpaRepository;

  @Override
  public State get(String dancerId) {
    Optional<StateJpaEntity> optionalStateJpaEntity = stateJpaRepository.findById(dancerId);
    if (optionalStateJpaEntity.isPresent()) {
      log.info("Getting...");
      return StateMapper.of(optionalStateJpaEntity.get());
    } else {
      log.info("Creating...");
      return new State();
    }
  }

  @Override
  public void save(State state, String dancerId) {
    StateJpaEntity stateJpaEntity = new StateJpaEntity();
    stateJpaEntity.setDancerId(dancerId);
    StateJpaEntity.Data data = new StateJpaEntity.Data();
    data.setValue("foo");
    stateJpaEntity.setData(data);
    stateJpaRepository.save(stateJpaEntity);
  }
}
