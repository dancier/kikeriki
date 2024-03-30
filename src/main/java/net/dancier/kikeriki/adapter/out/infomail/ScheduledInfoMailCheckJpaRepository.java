package net.dancier.kikeriki.adapter.out.infomail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ScheduledInfoMailCheckJpaRepository extends JpaRepository<ScheduledInfoMailCheckJpaEntity, UUID> {

  List<ScheduledInfoMailCheckJpaEntity> findByDancerIdAndStatus(String dancerId, ScheduledInfoMailCheckJpaEntity.STATUS status);

}
