package net.dancier.kikeriki.application.port;

import java.time.LocalDateTime;

public interface ScheduleInfomailCheckPort {
  void schedule(LocalDateTime when, String dancerId);

}
