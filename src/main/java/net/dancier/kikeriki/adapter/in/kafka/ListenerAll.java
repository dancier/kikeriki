package net.dancier.kikeriki.adapter.in.kafka;

import io.cloudevents.CloudEvent;
import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.application.domain.model.events.MessagePostedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ListenerAll {

  private static final Logger log = LoggerFactory.getLogger(ListenerAll.class);

  private final ApplicationEventPublisher applicationEventPublisher;

  @KafkaListener(topics = {
    "message-posted",
    "chat-created",
    "message-read",
    "profile-updated"
  })
  void listener(CloudEvent cloudEvent) {
    log.info("Got this event....");
    String businessEvent = cloudEvent.getType();

    switch (businessEvent) {
      case "message-posted" -> messagePostedEvent(cloudEvent);
      default -> {}
    }
    log.info(cloudEvent.toString());

  }

  private void messagePostedEvent(CloudEvent cloudEvent) {
    applicationEventPublisher.publishEvent(new MessagePostedEvent());
  }
}
