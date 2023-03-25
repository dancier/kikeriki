package net.dancier.kikeriki.state;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.*;


@RequiredArgsConstructor
@EqualsAndHashCode(of = "dancerId")
@ToString
public class DancerState
{
  @Getter
  private final UUID dancerId;
  private final Set<UUID> unseenMessages = new HashSet<>();

  @Getter
  private Optional<ZonedDateTime> lastMailSent = Optional.empty();
  @Getter
  private Optional<ZonedDateTime> lastInvolvement = Optional.empty();


  public void setLastLogin(ZonedDateTime timestamp)
  {
    lastInvolvement = Optional.of(timestamp);
    unseenMessages.clear();
    lastMailSent = Optional.empty();
  }

  public void addUnreadChatMessage(UUID id)
  {
    unseenMessages.add(id);
  }

  public void markChatMessagesAsSeen(ZonedDateTime timestamp)
  {
    lastInvolvement = Optional.of(timestamp);
    unseenMessages.clear();
    lastMailSent = Optional.empty();
  }

  public void setLastMailSent(ZonedDateTime timestamp)
  {
    lastMailSent = Optional.of(timestamp);
  }

  public Set<UUID> getUnseenMessages()
  {
    return Collections.unmodifiableSet(unseenMessages);
  }
}
