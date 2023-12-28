package net.dancier.kikeriki.adapter.in.kafka;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.application.domain.model.events.EmailSendingRequestedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class MailCommandListener {

  private static final Logger log = LoggerFactory.getLogger(MailCommandListener.class);

  private final ObjectMapper objectMapper;

  private final MailSender mailSender;

  @KafkaListener(topics = {"email-sending-requested"})
  void listener(CloudEvent cloudEvent) {
    log.info("Got Mail Command: " + cloudEvent);
    JsonNode simpleMailMessage = null;
    try {
      simpleMailMessage = objectMapper.readValue(cloudEvent.getData().toBytes(), JsonNode.class);
      log.info("Transformed: " + simpleMailMessage);
      EmailSendingRequestedEvent bla = objectMapper.readValue(cloudEvent.getData().toBytes(), EmailSendingRequestedEvent.class);
      log.info("The mailmessage: " + bla);
      mailSender.send(bla);
    } catch (IOException ioe) {
      log.info(ioe.toString());
    }
  }
}
