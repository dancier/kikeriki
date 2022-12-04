package net.dancier.kikeriki.state;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Data
@RequiredArgsConstructor
public class DancerInvolvement
{
  private final UUID dancerId;
  private final Set<UUID> unreadMessages = new HashSet<>();

  private ZonedDateTime lastMailSent = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("Europe/Berlin"));
  private ZonedDateTime lastLogin = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("Europe/Berlin"));
  private ZonedDateTime lastMessageRead = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("Europe/Berlin"));

  public boolean addUnreadMessage(UUID id)
  {
    return unreadMessages.add(id);
  }

  public boolean markMessageAsRead(UUID id)
  {
    return unreadMessages.remove(id);
  }
}
