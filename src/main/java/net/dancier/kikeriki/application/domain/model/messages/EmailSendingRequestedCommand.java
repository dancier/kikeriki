package net.dancier.kikeriki.application.domain.model.messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.mail.SimpleMailMessage;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class EmailSendingRequestedCommand {
  private final String id;
  private final String[] to;
  private final String from;
  private final String[] cc;
  private final String[] bcc;
  private final String subject;
  private final String text;

  public static class EmailSendingRequestedCommandBuilder {
    private String id;
    private String[] to;
    private String from;
    private String[] cc;
    private String[] bcc;
    private String subject;
    private String text;

    public EmailSendingRequestedCommandBuilder setId(String id) {
      this.id = id;
      return this;
    }

    public EmailSendingRequestedCommandBuilder setTo(String[] to) {
      this.to = to;
      return this;
    }

    public EmailSendingRequestedCommandBuilder setFrom(String from) {
      this.from = from;
      return this;
    }

    public EmailSendingRequestedCommandBuilder setCc(String[] cc) {
      this.cc = cc;
      return this;
    }

    public EmailSendingRequestedCommandBuilder setBcc(String[] bcc) {
      this.bcc = bcc;
      return this;
    }

    public EmailSendingRequestedCommandBuilder setSubject(String subject) {
      this.subject = subject;
      return this;
    }

    public EmailSendingRequestedCommandBuilder setText(String text) {
      this.text = text;
      return this;
    }
    public EmailSendingRequestedCommand build() {
      return new EmailSendingRequestedCommand(id, to, from, cc, bcc, subject, text);
    }
  }
}
