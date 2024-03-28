package net.dancier.kikeriki.adapter.out.infomail;

import net.dancier.kikeriki.application.port.ScheduleInfomailCheck;

import java.time.LocalDateTime;

public class InfomailServiceAdapter implements ScheduleInfomailCheck {
  @Override
  public void schedule(LocalDateTime when, String dancerId) {

  }
}
