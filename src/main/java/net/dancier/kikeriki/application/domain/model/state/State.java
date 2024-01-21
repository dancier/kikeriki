package net.dancier.kikeriki.application.domain.model.state;

import lombok.NonNull;
import net.dancier.kikeriki.application.port.StateDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


public class State {

  private List<UnreadChatMessage> unreadChatMessages = new LinkedList<>();

  private Set<String> pendingReadMessages = new HashSet<>();

  private Optional<MailMessage> optSentLastMessage = Optional.empty();

  public void setLastMailMessage(@NonNull MailMessage mailMessage) {
    this.optSentLastMessage = Optional.of(mailMessage);
  }

  public Optional<LocalDateTime> getLastTimeMailWasSent() {
    return optSentLastMessage.map(MailMessage::getCreatedAt);
  }

  public Boolean allowedToSendAnotherMail(LocalDate now) {
    if (optSentLastMessage.isEmpty()) {
      return true;
    } else {
      return getLastTimeMailWasSent().get().toLocalDate().isBefore(now);
    }
  }

  public void addUnreadChatMessage(UnreadChatMessage unreadChatMessage) {
    if (!this.unreadChatMessages.contains(unreadChatMessage)) {
      this.unreadChatMessages.add(unreadChatMessage);
    }
  }

  public void removeReadMessages(String messageId) {
    this.unreadChatMessages.removeIf(unreadChatMessage -> unreadChatMessage.getChatMessageId().equals(messageId));
    this.pendingReadMessages.add(messageId);
  }

  public Integer unreadMessagesCount() {
    return this.unreadChatMessages.size();
  }

  public Boolean hasUnreadMessagesAfter(LocalDateTime thisTime) {
    return this
      .unreadChatMessages
      .stream()
      .anyMatch(unreadChatMessage -> unreadChatMessage.getCreatedAt().isAfter(thisTime));
  }

  public Boolean isCandidateForSendingMail(LocalDateTime forGivenDate) {
    return allowedToSendAnotherMail(forGivenDate.toLocalDate())
        && hasUnreadMessagesAfter(forGivenDate);
  }

  public StateDto toDto() {
    StateDto stateDto = new StateDto();
    stateDto.setUnreadChatMessages(this.unreadChatMessages);
    stateDto.setOptSendlastMessage(this.optSentLastMessage);
    stateDto.setPendingReadMessages(this.pendingReadMessages);
    return stateDto;
  }
}
