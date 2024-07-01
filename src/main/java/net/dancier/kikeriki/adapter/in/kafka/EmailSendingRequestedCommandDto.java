package net.dancier.kikeriki.adapter.in.kafka;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.mail.SimpleMailMessage;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailSendingRequestedCommandDto extends SimpleMailMessage {
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
