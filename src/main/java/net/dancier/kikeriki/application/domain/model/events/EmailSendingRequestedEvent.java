package net.dancier.kikeriki.application.domain.model.events;

import org.springframework.mail.SimpleMailMessage;

public class EmailSendingRequestedEvent extends SimpleMailMessage {
  public void setTo(String[] in) {
    super.setTo(in);
  }
}
