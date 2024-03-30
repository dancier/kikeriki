package net.dancier.kikeriki.adapter.out.infomail;

import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.adapter.out.mail.MailOutboxJpaEntity;
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

  @Scheduled(fixedRate = 5000L)
  public void check() {
    log.info("checking");
    Collection<ScheduledInfoMailCheckJpaEntity> mailOutboxJpaEntities = scheduledInfoMailCheckJpaRepository.lockAndList();
    log.info("After the check..." + mailOutboxJpaEntities);
    log.info("Got: " + mailOutboxJpaEntities.size());
  }
}
