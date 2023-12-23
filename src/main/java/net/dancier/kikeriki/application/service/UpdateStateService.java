package net.dancier.kikeriki.application.service;

import net.dancier.kikeriki.application.domain.model.events.ChatCreatedEvent;
import net.dancier.kikeriki.application.domain.model.events.MessagePostedEvent;
import org.springframework.stereotype.Component;

@Component
public class UpdateStateService {

  public void handle(MessagePostedEvent messagePostedDto) {

  }

  public void handle(ChatCreatedEvent chatCreatedEvent) {

  }

}
