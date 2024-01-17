package net.dancier.kikeriki.adapter.out.persistence.state;

import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.application.domain.model.state.State;
import net.dancier.kikeriki.application.port.StatePort;
import org.hibernate.FetchNotFoundException;
import org.hibernate.annotations.NotFound;
import org.springframework.stereotype.Component;

import java.util.UUID;


@RequiredArgsConstructor
@Component
public class PostgreSQLStateAdapter implements StatePort {

  private final StateJpaRepository stateJpaRepository;

  @Override
  public State get(String dancerId) {
    StateJpaEntity stateJpaEntity = stateJpaRepository.findById(dancerId).orElseThrow( () -> new IllegalStateException());

    return new State();
  }

  @Override
  public void save(State state, String dancerId) {
    StateJpaEntity stateJpaEntity = new StateJpaEntity();
    stateJpaEntity.setDancer_id(dancerId);
    StateJpaEntity.Data data = new StateJpaEntity.Data();
    data.setValue("foo");
    stateJpaEntity.setData(data);
    stateJpaRepository.save(stateJpaEntity);
  }
}
