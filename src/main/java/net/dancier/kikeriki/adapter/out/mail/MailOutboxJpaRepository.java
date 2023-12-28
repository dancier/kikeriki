package net.dancier.kikeriki.adapter.out.mail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MailOutboxJpaRepository extends JpaRepository<MailOutboxJpaEntity, UUID> {
}
