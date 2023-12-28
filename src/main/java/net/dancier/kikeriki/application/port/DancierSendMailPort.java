package net.dancier.kikeriki.application.port;

import net.dancier.kikeriki.application.domain.model.events.EmailSendingRequestedEvent;

public interface DancierSendMailPort {

  void schedule(EmailSendingRequestedEvent emailSendingRequestedEvent);

}
