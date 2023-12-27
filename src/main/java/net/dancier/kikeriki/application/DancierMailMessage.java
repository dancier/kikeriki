package net.dancier.kikeriki.application;

import org.springframework.mail.SimpleMailMessage;

public class DancierMailMessage extends SimpleMailMessage {
  public void setTo(String[] in) {
    super.setTo(in);
  }
}
