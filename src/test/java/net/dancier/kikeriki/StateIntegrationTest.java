package net.dancier.kikeriki;

import net.dancier.kikeriki.application.domain.model.state.State;
import net.dancier.kikeriki.application.port.StatePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class StateIntegrationTest extends NeededInfrastructureBaseTestClass {

  @Autowired
  ApplicationEventPublisher applicationEventPublisher;

  @Value("${spring.datasource.url}")
  private String datasource;

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;


  @Autowired
  StatePort statePort;

  @Test
  public void newUnreadMessage() {
    assertThat(statePort.get(ApplicationEventStubbing.RECIPIENT_ID).unreadMessagesCount()).isEqualTo(0);

    applicationEventPublisher.publishEvent(ApplicationEventStubbing.messagePostedEvent());

    State resultingState = statePort.get(ApplicationEventStubbing.RECIPIENT_ID);

    assertThat(resultingState.unreadMessagesCount()).isEqualTo(1);
    assertThat(resultingState.getLastTimeOfInfomail()).isEmpty();
    assertThat(resultingState.isCandidateForSendingMail(LocalDateTime.now())).isTrue();
  }

}
