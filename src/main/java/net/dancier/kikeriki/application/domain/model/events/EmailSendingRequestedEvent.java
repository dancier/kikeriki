package net.dancier.kikeriki.application.domain.model.events;

import org.springframework.mail.SimpleMailMessage;

import java.util.UUID;

public class EmailSendingRequestedEvent extends SimpleMailMessage {
  private String id;
  public void setId(String id) {
    this.id = id;
  }
  public String getId() {
    return this.id;
  }

  public void setTo(String[] in) {
    super.setTo(in);
  }
}
