package net.dancier.kikeriki.application.domain.model.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.mail.SimpleMailMessage;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

  public void setCc(String[] in) {
    super.setCc(in);
  }
}
