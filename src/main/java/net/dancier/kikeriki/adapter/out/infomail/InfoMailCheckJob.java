package net.dancier.kikeriki.adapter.out.infomail;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.application.CheckAndSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class InfoMailCheckJob {
  private static final Logger log = LoggerFactory.getLogger(InfoMailCheckJob.class);

  private final ScheduledInfoMailCheckJpaRepository scheduledInfoMailCheckJpaRepository;

  private final CheckAndSendService checkAndSendService;

  @Scheduled(fixedRate = 5000L)
  public void check() {
    log.info("checking");
    Collection<ScheduledInfoMailCheckJpaEntity> scheduledEntities = scheduledInfoMailCheckJpaRepository.lockAndList();
    log.info("After the check..." + scheduledEntities);
    for(ScheduledInfoMailCheckJpaEntity scheduledInfoMailCheckJpaEntity: scheduledEntities) {
      checkAndSend(scheduledInfoMailCheckJpaEntity);
    }
  }

  private void checkAndSend(ScheduledInfoMailCheckJpaEntity scheduledInfoMailCheckJpaEntity) {
    try {
      checkAndSendService.checkAndSend(scheduledInfoMailCheckJpaEntity.getDancerId());
      log.info("Sucess setting status to done");
      scheduledInfoMailCheckJpaEntity.setStatus(ScheduledInfoMailCheckJpaEntity.STATUS.DONE);
    } catch (Exception exception) {
      log.info("Failure setting status to failed: " + exception);
      scheduledInfoMailCheckJpaEntity.setStatus(ScheduledInfoMailCheckJpaEntity.STATUS.FINALLY_FAILED);
    } finally {
      scheduledInfoMailCheckJpaRepository.save(scheduledInfoMailCheckJpaEntity);
    }
  }
}
