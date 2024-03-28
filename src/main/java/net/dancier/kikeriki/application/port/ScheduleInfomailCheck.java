package net.dancier.kikeriki.application.port;

import java.time.LocalDateTime;

public interface ScheduleInfomailCheck {
  void schedule(LocalDateTime when, String dancerId);

}
