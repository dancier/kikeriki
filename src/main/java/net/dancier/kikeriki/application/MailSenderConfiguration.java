package net.dancier.kikeriki.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class MailSenderConfiguration {

  @Bean
  public JavaMailSender getJavaMailSender() {
      JavaMailSender javaMailSender = new DumpingMailSender();
      return javaMailSender;
  }

}
