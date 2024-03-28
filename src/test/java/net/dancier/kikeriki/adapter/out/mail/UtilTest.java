package net.dancier.kikeriki.adapter.out.mail;

import net.dancier.kikeriki.application.domain.model.messages.EmailSendingRequestedCommand;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UtilTest {

  @Test
  public void testCommandToSimpleMailMessage() {
    EmailSendingRequestedCommand command = new EmailSendingRequestedCommand.EmailSendingRequestedCommandBuilder()
      .setId(UUID.randomUUID().toString())
      .setTo(new String[]{"bar"})
      .setFrom("foo")
      .setSubject("why")
      .setText("reson")
      .build();

    SimpleMailMessage result = Util.commandToSimpleMailMessage(command);

    assertThat(result).isNotNull();
    assertThat(result.getTo()).isNotEmpty();
    assertThat(result.getFrom()).isEqualTo("foo");
    assertThat(result.getSubject()).isEqualTo("why");
    assertThat(result.getText()).isEqualTo("reson");
  }

}