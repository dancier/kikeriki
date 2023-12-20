package net.dancier.kikeriki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ChatListener {

  Logger log = LoggerFactory.getLogger(ChatListener.class);

  @KafkaListener(topics = "message-posted")
  void listener(String data) {
    log.info("Got this event....");
    log.info(data);
  }
}
