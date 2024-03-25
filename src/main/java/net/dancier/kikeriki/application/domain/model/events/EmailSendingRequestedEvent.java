package net.dancier.kikeriki.application.domain.model.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailSendingRequestedEvent extends SimpleMailMessage {
  private String id;
  public void setId(String id) {
    this.id = id;
  }
  public String getId() {
    return this.id;
  }

  public void setTo(String[] to) {
    super.setTo(to);
  }

  public void setCc(String[] cc) {
    super.setCc(cc);
  }

  public void setBcc(String[] bcc) {
    super.setBcc(bcc);
  }
}
