package net.dancier.kikeriki.application;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class MailCommandListener {

  private static final Logger log = LoggerFactory.getLogger(MailCommandListener.class);

  private final ObjectMapper objectMapper;

  @KafkaListener(topics = {"email-sending-requested"})
  void listener(CloudEvent cloudEvent) {
    log.info("Got Mail Command: " + cloudEvent);
    SimpleMailMessage simpleMailMessage = null;
    try {
      simpleMailMessage = objectMapper.readValue(cloudEvent.getData().toBytes(), SimpleMailMessage.class);
    } catch (IOException ioe) {
      log.info(ioe.toString());
    }
  }
}
