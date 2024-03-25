package net.dancier.kikeriki.adapter.in.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
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
      EmailSendingRequestedCommandDto emailSendingRequestedCommand = objectMapper.readValue(cloudEvent.getData().toBytes(), EmailSendingRequestedCommandDto.class);
      log.info("Got that request to send a mail: {}", emailSendingRequestedCommand);
      emailSendingRequestedCommand.setId(cloudEvent.getId());
      applicationEventPublisher.publishEvent(emailSendingRequestedCommand);
    } catch (IOException ioe) {
      log.error(ioe.toString());
    }
  }
}
