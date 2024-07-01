package net.dancier.kikeriki.adapter.out.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailSenderConfiguration {

  private final Logger log = LoggerFactory.getLogger(MailSenderConfiguration.class);

  @Bean
  @Profile({"dev"})
  public JavaMailSender getJavaMailSender() {
      JavaMailSender javaMailSender = new MailSenderDumping();
      return javaMailSender;
  }

  @Bean
  @Profile("staging")
  public JavaMailSender getRealSender(
    @Value("${app.mail.host}") String hostname,
    @Value("${app.mail.port}") String port,
    @Value("${app.mail.user}") String user,
    @Value("${app.mail.pass}") String pass
  ) {
    log.info("Setup Mailsender with: {}/{}/{}", hostname, port, user);

    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
    javaMailSender.setHost(hostname);
    javaMailSender.setPort(Integer.valueOf(port));

    javaMailSender.setUsername(user);
    javaMailSender.setPassword(pass);

    Properties props = javaMailSender.getJavaMailProperties();
    props.put("mail.smtp.ssl.enable", "true");
    props.put("mail.debug", "true");

    return javaMailSender;
  }
}
