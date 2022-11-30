package net.dancier.kikeriki.state;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class DancerStateChatMessagesRepository {

  private Map<UUID,Instant> readAtLeastAt = new HashMap<>();

  private Map<UUID,Instant> lastUnreadMessageAt = new HashMap<>();

  public Optional<Instant> getReadAtLeastAt(UUID dancerId) {
    return Optional.ofNullable(readAtLeastAt.get(dancerId));
  }

  public Optional<Instant> getLastUnreadMessageAt(UUID dancerId) {
    return Optional.ofNullable(lastUnreadMessageAt.get(dancerId));
  }

  public void setReadAtLeastAt(UUID dancerId, Instant readTime) {
    readAtLeastAt.put(dancerId, readTime);
  }

  public void setLastUnreadMessageAt(UUID dancerId, Instant unreadMessageTime) {
    lastUnreadMessageAt.put(dancerId, unreadMessageTime);
  }

}
