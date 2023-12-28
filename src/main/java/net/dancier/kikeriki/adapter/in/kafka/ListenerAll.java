package net.dancier.kikeriki.adapter.in.kafka;

import io.cloudevents.CloudEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ListenerAll {

  Logger log = LoggerFactory.getLogger(ListenerAll.class);

  @KafkaListener(topics = {
    "message-posted",
    "chat-created",
    "message-read",
    "profile-updated"
  })
  void listener(CloudEvent cloudEvent) {
    log.info("Got this event....");
    log.info(cloudEvent.toString());

  }
}
