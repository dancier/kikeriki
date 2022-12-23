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
  public final static ZonedDateTime NEVER = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("Europe/Berlin"));


  private final UUID dancerId;
  private final Set<UUID> unreadMessages = new HashSet<>();

  private ZonedDateTime lastMailSent = NEVER;
  private ZonedDateTime lastLogin = NEVER;
  private ZonedDateTime lastMessageRead = NEVER;

  public boolean addUnreadMessage(UUID id)
  {
    return unreadMessages.add(id);
  }

  public boolean markMessageAsRead(UUID id)
  {
    return unreadMessages.remove(id);
  }

  public ZonedDateTime getLastInvolvement()
  {
    return lastLogin.isAfter(lastMessageRead) ? lastLogin : lastMessageRead;
  }

  public void clearLastMailSent()
  {
    lastMailSent = NEVER;
  }
}
