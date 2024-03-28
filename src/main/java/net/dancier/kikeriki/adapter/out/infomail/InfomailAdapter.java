package net.dancier.kikeriki.adapter.out.infomail;

import net.dancier.kikeriki.application.port.ScheduleInfomailCheckPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InfomailAdapter implements ScheduleInfomailCheckPort {

  Logger log = LoggerFactory.getLogger(InfomailAdapter.class);

  @Override
  public void schedule(LocalDateTime when, String dancerId) {
    log.info("Scheduling check!!!");
  }
}
