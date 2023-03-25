package net.dancier.kikeriki.state;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class DancerInvolvementTest
{
  @Test
  @DisplayName("A newly created instance should have no timestamp for a last involvment")
  public void testNewInvolvementHasNoLastInvolvementTimestamp()
  {
    // When
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());

    // Then
    assertThat(involvement.getLastInvolvement())
      .as("last involvement")
      .isEmpty();
  }

  @Test
  @DisplayName("A newly created instance should have no timestamp for a last mail sent")
  public void testNewInvolvementHasNoLastMailSentTimestamp()
  {
    // When
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());

    // Then
    assertThat(involvement.getLastMailSent())
      .as("last mail sent")
      .isEmpty();
  }

  @Test
  @DisplayName("A newly created instance should have no unseen chat-messages")
  public void testNewInvolvementHasNoUnseenChatMessages()
  {
    // When
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());

    // Then
    assertThat(involvement.getUnseenMessages())
      .as("unseen messages")
      .isEmpty();
  }

  @Test
  @DisplayName("If the ID of an unread chat-message is added, it should be added to the unseen messages")
  public void testAddUnreadChatMessageIsAddedToUnseenMessages()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    UUID messageId = UUID.randomUUID();

    // When
    involvement.addUnreadChatMessage(messageId);

    // Then
    assertThat(involvement.getUnseenMessages())
      .as("unseen messages")
      .contains(messageId);
  }

  @Test
  @DisplayName("If the ID of an unread chat-message is added, it should not change the timestamp of the last involvement")
  public void testAddUnreadChatMessageShouldNotChangeLastInvolvement()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());

    // When
    involvement.addUnreadChatMessage(UUID.randomUUID());

    // Then
    assertThat(involvement.getLastInvolvement())
      .as("last involvement")
      .isEmpty();
  }

  @Test
  @DisplayName("If a timestamp for the last login is set, the timestamp of the last involvement should reflect it")
  public void testSetLastLoginShouldBeReflectedInLastInvolvement()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    ZonedDateTime timestampLastLogin = ZonedDateTime.now();

    // When
    involvement.setLastLogin(timestampLastLogin);

    // Then
    assertThat(involvement.getLastInvolvement())
      .as("last involvement")
      .contains(timestampLastLogin);
  }

  @Test
  @DisplayName("If a timestamp for the last login is set, all messages should be considered as seen")
  public void testSetLastLoginMarksMessagesAsSeen()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    involvement.addUnreadChatMessage(UUID.randomUUID());

    // When
    involvement.setLastLogin(ZonedDateTime.now());

    // Then
    assertThat(involvement.getUnseenMessages())
      .as("unseen messages")
      .isEmpty();
  }

  @Test
  @DisplayName("If a timestamp for the last login is set, the timestamp for the last mail sent is cleared")
  public void testSetLastLoginResetsLastMailSent()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    involvement.setLastMailSent(ZonedDateTime.now());

    // When
    involvement.setLastLogin(ZonedDateTime.now());

    // Then
    assertThat(involvement.getLastMailSent())
      .as("last mail sent")
      .isEmpty();
  }

  @Test
  @DisplayName("If the chat-messages are marked as seen, the according timestamp is remembered as last involvement")
  public void testMarkChatMessagesAsSeenShouldBeRememberedAsLastInvolvement()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    ZonedDateTime timestampMessageRead = ZonedDateTime.now();

    // When
    involvement.markChatMessagesAsSeen(timestampMessageRead);

    // Then
    assertThat(involvement.getLastInvolvement())
      .as("last involvement")
      .contains(timestampMessageRead);
  }

  @Test
  @DisplayName("If the chat-messages are marked as seen, the set of unseen messages should be cleared")
  public void testMarkChatMessagesAsSeenShouldClearTheUnseenMessages()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    involvement.addUnreadChatMessage(UUID.randomUUID());

    // When
    involvement.markChatMessagesAsSeen(ZonedDateTime.now());

    // Then
    assertThat(involvement.getUnseenMessages())
      .as("unseen messages")
      .isEmpty();
  }

  @Test
  @DisplayName("If the chat-messages are marked as seen, the timestamp for the last mail sent is cleard")
  public void testMarkChatMessagesAsSeenResetsLastMailSent()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    involvement.setLastMailSent(ZonedDateTime.now());

    // When
    involvement.markChatMessagesAsSeen(ZonedDateTime.now());

    // Then
    assertThat(involvement.getLastMailSent())
      .as("last mail sent")
      .isEmpty();
  }

  @Test
  @DisplayName("If a timestamp for the last sent mail is set, it should be stored")
  public void testSetLastMailSentIsStored()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    ZonedDateTime timestampLastMailSent = ZonedDateTime.now();

    // When
    involvement.setLastMailSent(timestampLastMailSent);

    // Then
    assertThat(involvement.getLastMailSent())
      .as("last sent mail")
      .contains(timestampLastMailSent);
  }

  @Test
  @DisplayName("If a timestamp for the last sent mail is set, it should not change the last involvement")
  public void testSetLastMailSentDoesNotChangeTheLastInvolvement()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());

    // When
    involvement.setLastMailSent(ZonedDateTime.now());

    // Then
    assertThat(involvement.getLastInvolvement())
      .as("last involvement")
      .isEmpty();
  }

  @Test
  @DisplayName("If a timestamp for the last sent mail is set, the unseen chant-messages should not be cleared")
  public void testSetLastMailSentDoesNotClearUnseenMessages()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    involvement.addUnreadChatMessage(UUID.randomUUID());

    // When
    involvement.setLastMailSent(ZonedDateTime.now());

    // Then
    assertThat(involvement.getUnseenMessages())
      .as("unseen messages")
      .isNotEmpty();
  }
}
