package net.dancier.kikeriki.state;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class DancerStateTest
{
  @Test
  @DisplayName("A newly created instance should have no timestamp for a last involvment")
  public void testNewInvolvementHasNoLastInvolvementTimestamp()
  {
    // When
    DancerState dancerState = new DancerState(UUID.randomUUID());

    // Then
    assertThat(dancerState.getLastInvolvement())
      .as("last involvement")
      .isEmpty();
  }

  @Test
  @DisplayName("A newly created instance should have no timestamp for a last mail sent")
  public void testNewInvolvementHasNoLastMailSentTimestamp()
  {
    // When
    DancerState dancerState = new DancerState(UUID.randomUUID());

    // Then
    assertThat(dancerState.getLastMailSent())
      .as("last mail sent")
      .isEmpty();
  }

  @Test
  @DisplayName("A newly created instance should have no unseen chat-messages")
  public void testNewInvolvementHasNoUnseenChatMessages()
  {
    // When
    DancerState dancerState = new DancerState(UUID.randomUUID());

    // Then
    assertThat(dancerState.getUnseenMessages())
      .as("unseen messages")
      .isEmpty();
  }

  @Test
  @DisplayName("If the ID of an unread chat-message is added, it should be added to the unseen messages")
  public void testAddUnreadChatMessageIsAddedToUnseenMessages()
  {
    // Given
    DancerState dancerState = new DancerState(UUID.randomUUID());
    UUID messageId = UUID.randomUUID();

    // When
    dancerState.addUnreadChatMessage(messageId);

    // Then
    assertThat(dancerState.getUnseenMessages())
      .as("unseen messages")
      .contains(messageId);
  }

  @Test
  @DisplayName("If the ID of an unread chat-message is added, it should not change the timestamp of the last involvement")
  public void testAddUnreadChatMessageShouldNotChangeLastInvolvement()
  {
    // Given
    DancerState dancerState = new DancerState(UUID.randomUUID());

    // When
    dancerState.addUnreadChatMessage(UUID.randomUUID());

    // Then
    assertThat(dancerState.getLastInvolvement())
      .as("last involvement")
      .isEmpty();
  }

  @Test
  @DisplayName("If a timestamp for the last login is set, the timestamp of the last involvement should reflect it")
  public void testSetLastLoginShouldBeReflectedInLastInvolvement()
  {
    // Given
    DancerState dancerState = new DancerState(UUID.randomUUID());
    ZonedDateTime timestampLastLogin = ZonedDateTime.now();

    // When
    dancerState.setLastLogin(timestampLastLogin);

    // Then
    assertThat(dancerState.getLastInvolvement())
      .as("last involvement")
      .contains(timestampLastLogin);
  }

  @Test
  @DisplayName("If a timestamp for the last login is set, all messages should be considered as seen")
  public void testSetLastLoginMarksMessagesAsSeen()
  {
    // Given
    DancerState dancerState = new DancerState(UUID.randomUUID());
    dancerState.addUnreadChatMessage(UUID.randomUUID());

    // When
    dancerState.setLastLogin(ZonedDateTime.now());

    // Then
    assertThat(dancerState.getUnseenMessages())
      .as("unseen messages")
      .isEmpty();
  }

  @Test
  @DisplayName("If a timestamp for the last login is set, the timestamp for the last mail sent is cleared")
  public void testSetLastLoginResetsLastMailSent()
  {
    // Given
    DancerState dancerState = new DancerState(UUID.randomUUID());
    dancerState.setLastMailSent(ZonedDateTime.now());

    // When
    dancerState.setLastLogin(ZonedDateTime.now());

    // Then
    assertThat(dancerState.getLastMailSent())
      .as("last mail sent")
      .isEmpty();
  }

  @Test
  @DisplayName("If the chat-messages are marked as seen, the according timestamp is remembered as last involvement")
  public void testMarkChatMessagesAsSeenShouldBeRememberedAsLastInvolvement()
  {
    // Given
    DancerState dancerState = new DancerState(UUID.randomUUID());
    ZonedDateTime timestampMessageRead = ZonedDateTime.now();

    // When
    dancerState.markChatMessagesAsSeen(timestampMessageRead);

    // Then
    assertThat(dancerState.getLastInvolvement())
      .as("last involvement")
      .contains(timestampMessageRead);
  }

  @Test
  @DisplayName("If the chat-messages are marked as seen, the set of unseen messages should be cleared")
  public void testMarkChatMessagesAsSeenShouldClearTheUnseenMessages()
  {
    // Given
    DancerState dancerState = new DancerState(UUID.randomUUID());
    dancerState.addUnreadChatMessage(UUID.randomUUID());

    // When
    dancerState.markChatMessagesAsSeen(ZonedDateTime.now());

    // Then
    assertThat(dancerState.getUnseenMessages())
      .as("unseen messages")
      .isEmpty();
  }

  @Test
  @DisplayName("If the chat-messages are marked as seen, the timestamp for the last mail sent is cleard")
  public void testMarkChatMessagesAsSeenResetsLastMailSent()
  {
    // Given
    DancerState dancerState = new DancerState(UUID.randomUUID());
    dancerState.setLastMailSent(ZonedDateTime.now());

    // When
    dancerState.markChatMessagesAsSeen(ZonedDateTime.now());

    // Then
    assertThat(dancerState.getLastMailSent())
      .as("last mail sent")
      .isEmpty();
  }

  @Test
  @DisplayName("If a timestamp for the last sent mail is set, it should be stored")
  public void testSetLastMailSentIsStored()
  {
    // Given
    DancerState dancerState = new DancerState(UUID.randomUUID());
    ZonedDateTime timestampLastMailSent = ZonedDateTime.now();

    // When
    dancerState.setLastMailSent(timestampLastMailSent);

    // Then
    assertThat(dancerState.getLastMailSent())
      .as("last sent mail")
      .contains(timestampLastMailSent);
  }

  @Test
  @DisplayName("If a timestamp for the last sent mail is set, it should not change the last involvement")
  public void testSetLastMailSentDoesNotChangeTheLastInvolvement()
  {
    // Given
    DancerState dancerState = new DancerState(UUID.randomUUID());

    // When
    dancerState.setLastMailSent(ZonedDateTime.now());

    // Then
    assertThat(dancerState.getLastInvolvement())
      .as("last involvement")
      .isEmpty();
  }

  @Test
  @DisplayName("If a timestamp for the last sent mail is set, the unseen chant-messages should not be cleared")
  public void testSetLastMailSentDoesNotClearUnseenMessages()
  {
    // Given
    DancerState dancerState = new DancerState(UUID.randomUUID());
    dancerState.addUnreadChatMessage(UUID.randomUUID());

    // When
    dancerState.setLastMailSent(ZonedDateTime.now());

    // Then
    assertThat(dancerState.getUnseenMessages())
      .as("unseen messages")
      .isNotEmpty();
  }
}
