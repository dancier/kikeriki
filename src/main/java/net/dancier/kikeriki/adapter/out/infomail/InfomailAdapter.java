package net.dancier.kikeriki.adapter.out.infomail;

import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.application.port.ScheduleInfomailCheckPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class InfomailAdapter implements ScheduleInfomailCheckPort {

  private final static Logger log = LoggerFactory.getLogger(InfomailAdapter.class);

  private final ScheduledInfoMailCheckJpaRepository scheduledInfoMailCheckJpaRepository;

  @Override
  public void schedule(LocalDateTime when, String dancerId) {
    Objects.requireNonNull(when);
    Objects.requireNonNull(dancerId);
    log.info("Scheduling check!!!");

    if (scheduledInfoMailCheckJpaRepository.findByDancerIdAndStatus(dancerId, ScheduledInfoMailCheckJpaEntity.STATUS.NEW).isEmpty()) {
      log.info("Adding new check...");
      ScheduledInfoMailCheckJpaEntity scheduledInfoMailCheckJpaEntity = new ScheduledInfoMailCheckJpaEntity();
      scheduledInfoMailCheckJpaEntity.setStatus(ScheduledInfoMailCheckJpaEntity.STATUS.NEW);
      scheduledInfoMailCheckJpaEntity.setDancerId(dancerId);
      scheduledInfoMailCheckJpaEntity.setCheckAt(when);

      scheduledInfoMailCheckJpaRepository.save(scheduledInfoMailCheckJpaEntity);
    } else {
      log.info("Skip adding a new check, as we have one already");
    }

  }
}
