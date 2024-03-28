package net.dancier.kikeriki.adapter.out.infomail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InfoMailCheckJob {
  private static final Logger log = LoggerFactory.getLogger(InfoMailCheckJob.class);

  @Scheduled(fixedRate = 5000L)
  public void recheck() {
    log.info("rechecking");
  }
}
