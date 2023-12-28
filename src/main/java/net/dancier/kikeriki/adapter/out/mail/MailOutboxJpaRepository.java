package net.dancier.kikeriki.adapter.out.mail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.UUID;

public interface MailOutboxJpaRepository extends JpaRepository<MailOutboxJpaEntity, String> {

  @Query(
    value = """
                UPDATE mail_outbox
                      SET status = 'IN_PROGRESS'
                    WHERE id IN (
                      SELECT id
                        FROM mail_outbox
                       WHERE status = 'NEW'
                       LIMIT 1
                    FOR UPDATE
                    )
                  RETURNING *;                    """,
    nativeQuery = true
  )
  Collection<MailOutboxJpaEntity> lockAndList();


}
