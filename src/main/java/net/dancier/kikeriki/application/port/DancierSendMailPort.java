package net.dancier.kikeriki.application.port;

import net.dancier.kikeriki.application.domain.model.messages.EmailSendingRequestedCommand;

public interface DancierSendMailPort {

  void schedule(EmailSendingRequestedCommand emailSendingRequestedCommand);

}
