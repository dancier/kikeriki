package net.dancier.kikeriki.application.domain.model.state;

import lombok.NonNull;
import net.dancier.kikeriki.application.port.StateDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


public class State {

  private List<UnreadChatMessage> unreadChatMessages = new LinkedList<>();

  private Set<String> pendingReadMessages = new HashSet<>();

  private Optional<InfoMail> optLastTimeCustomerWasInformed = Optional.empty();

  public void setLastMailMessage(@NonNull InfoMail infoMail) {
    this.optLastTimeCustomerWasInformed = Optional.of(infoMail);
  }

  public Optional<LocalDateTime> getLastTimeOfInfoMail() {
    return optLastTimeCustomerWasInformed.map(InfoMail::getCreatedAt);
  }

  public Boolean allowedToSendAnotherInfomail(LocalDate now) {
    if (optLastTimeCustomerWasInformed.isEmpty()) {
      return true;
    } else {
      return getLastTimeOfInfoMail().get().toLocalDate().isBefore(now);
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

  public Boolean hastUnreadMessagesSinceLastInfomail() {
    if (getLastTimeOfInfoMail().isPresent()) {
      return this
        .unreadChatMessages
        .stream()
        .anyMatch(unreadChatMessage -> unreadChatMessage.getCreatedAt().isAfter(getLastTimeOfInfoMail().get()));
    } else {
      return (unreadChatMessages.size() > 0);
    }
  }

  public Boolean isCandidateForSendingMail(LocalDateTime forGivenDate) {
    return allowedToSendAnotherInfomail(forGivenDate.toLocalDate())
        && hastUnreadMessagesSinceLastInfomail();
  }

  public StateDto toDto() {
    StateDto stateDto = new StateDto();
    stateDto.setUnreadChatMessages(this.unreadChatMessages);
    stateDto.setOptSendlastMessage(this.optLastTimeCustomerWasInformed);
    stateDto.setPendingReadMessages(this.pendingReadMessages);
    return stateDto;
  }
}
