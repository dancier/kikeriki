package net.dancier.kikeriki.adapter.out.infomail;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "scheduled_at")
@NoArgsConstructor
public class ScheduledInfoMailCheckJpaEntity {

  @Id
  @GeneratedValue
  private UUID id;

  @NotNull
  @Enumerated(EnumType.STRING)
  private STATUS status;

  @NotNull
  private String dancerId;

  @NotNull
  private LocalDateTime checkAt;

  public static enum STATUS {
    NEW,
    TEMPORARY_FAILED,
    FINALLY_FAILED,
    DONE
  }
}
