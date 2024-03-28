package net.dancier.kikeriki.adapter.out.mail;

import net.dancier.kikeriki.application.domain.model.messages.EmailSendingRequestedCommand;
import org.springframework.mail.SimpleMailMessage;

public class Util {

  public static SimpleMailMessage commandToSimpleMailMessage(EmailSendingRequestedCommand command) {
    SimpleMailMessage result = new SimpleMailMessage();
    result.setTo(command.getTo());
    result.setFrom(command.getFrom());
    result.setSubject(command.getSubject());
    result.setText(command.getText());
    return result;
  }
}
