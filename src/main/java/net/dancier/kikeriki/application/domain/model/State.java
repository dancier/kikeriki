package net.dancier.kikeriki.application.domain.model;

import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public class State {

  private List<UnreadChatMessage> unreadChatMessages = new LinkedList<>();

  private Optional<MailMessage> optSentLastMessage = Optional.empty();

  public void setLastMessage(@NonNull MailMessage mailMessage) {
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
}
