package net.dancier.kikeriki.adapter.out.mail;

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
