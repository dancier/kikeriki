package net.dancier.kikeriki.state;

import lombok.RequiredArgsConstructor;
import net.dancier.kikeriki.state.appevent.ScheduleCheck;
import net.dancier.kikeriki.state.appevent.SendMail;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class DancerStateMachine {

  private final DancerStateChatMessagesRepository dancerStateChatMessagesRepository;

  private final DancerStateMailMessageRepository dancerStateMailMessageRepository;

  private final ApplicationEventPublisher applicationEventPublisher;

  public void check(UUID dancerid) {
      Optional<Instant> readAtLeastOptional = dancerStateChatMessagesRepository.getReadAtLeastAt(dancerid);
      Optional<Instant> unreadMessageAtOptional = dancerStateChatMessagesRepository.getLastUnreadMessageAt(dancerid);
      Optional<Instant> lastMailSendOptional = dancerStateMailMessageRepository.getLastMailSendAt(dancerid);

      Optional<Instant> lastUserInteractionOptional =
        Stream.concat(
          readAtLeastOptional.stream(),
          lastMailSendOptional.stream()
        ).max(Instant::compareTo);

      if (lastMailSendOptional.isPresent()) {
          if (lastMailSendOptional.get().isBefore(unreadMessageAtOptional.get())) {
            applicationEventPublisher.publishEvent(new SendMail());
          }
      }
  }


}
