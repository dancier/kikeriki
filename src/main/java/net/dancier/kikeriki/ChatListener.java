package net.dancier.kikeriki;

import io.cloudevents.CloudEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ChatListener {

  Logger log = LoggerFactory.getLogger(ChatListener.class);

  @KafkaListener(topics = {
    "message-posted",
    "chat-created",
    "message-read",
    "profile-updated",
    "email-sending-requested"})
  void listener(CloudEvent cloudEvent) {
    log.info("Got this event....");
    log.info(cloudEvent.toString());
  }
}
