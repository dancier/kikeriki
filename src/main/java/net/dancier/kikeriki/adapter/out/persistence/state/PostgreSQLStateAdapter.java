package net.dancier.kikeriki.adapter.out.persistence.state;

import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.application.domain.model.state.State;
import net.dancier.kikeriki.application.port.StatePort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;


@RequiredArgsConstructor
@Component
public class PostgreSQLStateAdapter implements StatePort {

  private final StateJpaRepository stateJpaRepository;

  @Override
  public State get(UUID dancerId) {
    return null;
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
