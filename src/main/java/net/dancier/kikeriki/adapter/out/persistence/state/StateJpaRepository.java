package net.dancier.kikeriki.adapter.out.persistence.state;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StateJpaRepository
  extends JpaRepository<StateJpaEntity, String> {}
