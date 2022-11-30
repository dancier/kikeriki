package net.dancier.kikeriki.state;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
@Component
public class DancerStateMailMessageRepository {

  private Map<UUID,Instant> lastMailSendAt = new HashMap<>();

  private Map<UUID,Integer> mailsSendToday = new HashMap<>();

  public Optional<Instant> getLastMailSendAt(UUID dancerId) {
    return Optional.ofNullable(lastMailSendAt.get(dancerId));
  }

  public void setLastMailSendAt(UUID dancerId, Instant lastMailMessageSend) {
    lastMailSendAt.put(dancerId, lastMailMessageSend);
  }
}
