package net.dancier.kikeriki.adapter.in.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.application.domain.model.messages.MessagePostedEvent;
import net.dancier.kikeriki.application.domain.model.messages.MessageReadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class StateRelatedEventListener {

  private static final Logger log = LoggerFactory.getLogger(StateRelatedEventListener.class);

  private final ApplicationEventPublisher applicationEventPublisher;

  private final ObjectMapper objectMapper;

  @KafkaListener(topics = {
    "message-posted",
    "message-read"
  })
  void listener(CloudEvent cloudEvent) throws IOException {
    log.info("Got this event....");
    String businessEvent = cloudEvent.getType();

    switch (businessEvent) {
      case "message-posted" -> messagePostedEvent(cloudEvent);
      case "message-read" -> messageReadEvent(cloudEvent);
      default -> {}
    }
    log.info(cloudEvent.toString());
  }

  private void messageReadEvent(CloudEvent cloudEvent) throws IOException {
    log.info("Making application event....");
    MessageReadEventDto messageReadEventDto = objectMapper.readValue(cloudEvent.getData().toBytes(), MessageReadEventDto.class);
    log.info("Got this: {}", messageReadEventDto);
    MessageReadEvent messageReadEvent = new MessageReadEvent();
    messageReadEvent.setMessageId(messageReadEventDto.messageId);
    messageReadEvent.setReaderId(messageReadEventDto.readerId);

    applicationEventPublisher.publishEvent(messageReadEvent);
  }

  private void messagePostedEvent(CloudEvent cloudEvent) throws IOException {
    MessagePostedEventDto messagePostedEventDto =
      objectMapper.readValue(cloudEvent.getData().toBytes(), MessagePostedEventDto.class);
    MessagePostedEvent messagePostedEvent = new MessagePostedEvent();
    messagePostedEvent.setCreatedAt(
      messagePostedEventDto.createdAt.toLocalDateTime()
    );
    messagePostedEvent.setMessageId(messagePostedEventDto.messageId);
    messagePostedEvent.setAuthorId(messagePostedEventDto.authorId);
    messagePostedEvent.setRecipients(
      messagePostedEventDto.participantIds.stream().filter(p -> !p.equals(messagePostedEventDto.authorId)).collect(Collectors.toList())
    );

    applicationEventPublisher.publishEvent(messagePostedEvent);
  }
}
