package net.dancier.kikeriki.adapter.out.infomail;

import net.dancier.kikeriki.adapter.out.mail.MailOutboxJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ScheduledInfoMailCheckJpaRepository extends JpaRepository<ScheduledInfoMailCheckJpaEntity, UUID> {

  List<ScheduledInfoMailCheckJpaEntity> findByDancerIdAndStatus(String dancerId, ScheduledInfoMailCheckJpaEntity.STATUS status);

  @Query(
    value = """
                UPDATE scheduled_at
                      SET status = 'IN_PROGRESS'
                    WHERE id IN (
                      SELECT id
                        FROM scheduled_at
                       WHERE status = 'NEW'
                       LIMIT 1
                       FOR UPDATE
                    )
                  RETURNING *;
              """,
    nativeQuery = true
  )
  Collection<ScheduledInfoMailCheckJpaEntity> lockAndList();

}
