package net.dancier.kikeriki.application;


import io.cloudevents.CloudEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MailCommandListener {

  Logger log = LoggerFactory.getLogger(MailCommandListener.class);

  @KafkaListener(topics = {"email-sending-requested"})
  void listener(CloudEvent cloudEvent) {
    log.info("Got Mail Command: " + cloudEvent);
  }
}
