package net.dancier.kikeriki.adapter.in.kafka;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.application.domain.model.events.EmailSendingRequestedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class MailCommandListener {

  private static final Logger log = LoggerFactory.getLogger(MailCommandListener.class);

  private final ObjectMapper objectMapper;

  private final ApplicationEventPublisher applicationEventPublisher;

  @KafkaListener(topics = {"email-sending-requested"})
  void listener(CloudEvent cloudEvent) {
    log.info("Got Mail Command: " + cloudEvent);
    try {
      EmailSendingRequestedEvent emailSendingRequestedEvent = objectMapper.readValue(cloudEvent.getData().toBytes(), EmailSendingRequestedEvent.class);
      log.info("Got that request to send a mail: {}", emailSendingRequestedEvent);
      emailSendingRequestedEvent.setId(cloudEvent.getId());
      applicationEventPublisher.publishEvent(emailSendingRequestedEvent);
    } catch (IOException ioe) {
      log.info(ioe.toString());
    }
  }
}
