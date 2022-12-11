package net.dancier.kikeriki.state;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@RequiredArgsConstructor
@EqualsAndHashCode(of = "dancerId")
@ToString
public class DancerInvolvement
{
  public final static ZonedDateTime NEVER = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("Europe/Berlin"));


  @Getter
  private final UUID dancerId;
  private final Set<UUID> unseenMessages = new HashSet<>();

  @Getter
  private ZonedDateTime lastMailSent = NEVER;
  @Getter
  private ZonedDateTime lastInvolvement = NEVER;


  public void setLastLogin(ZonedDateTime timestamp)
  {
    lastInvolvement = timestamp;
    unseenMessages.clear();
    lastMailSent = NEVER;
  }

  public void addUnreadChatMessage(UUID id)
  {
    unseenMessages.add(id);
  }

  public void markChatMessagesAsSeen(ZonedDateTime timestamp)
  {
    lastInvolvement = timestamp;
    unseenMessages.clear();
    lastMailSent = NEVER;
  }

  public void setLastMailSent(ZonedDateTime timestamp)
  {
    lastMailSent = timestamp;
  }

  public Set<UUID> getUnseenMessages()
  {
    return Collections.unmodifiableSet(unseenMessages);
  }
}
