package net.dancier.kikeriki.adapter.out.mail;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.dancier.kikeriki.application.domain.model.messages.EmailSendingRequestedCommand;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "mail_outbox")
@NoArgsConstructor
public class MailOutboxJpaEntity {

  @Id
  private String id;

  @Enumerated(EnumType.STRING)
  private STATUS status;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "mail")
  private EmailSendingRequestedCommand emailSendingRequestedCommand;

  private LocalDateTime createdAt;

  public static enum STATUS {
    NEW,
    IN_PROGRESS,

    TEMPORARY_FAILED,

    FINALLY_FAILED,

    DONE
  }

}
