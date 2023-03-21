package net.dancier.kikeriki.state;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class DancerInvolvementTest
{
  @Test
  @DisplayName("The last involvement of a newly created instance should be the unix-epoch")
  public void testNewInvolvementHasUnixEpochAsLastInvolvement()
  {
    // When
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());

    // Then
    assertThat(involvement.getLastInvolvement())
      .describedAs("The last involvement should equal the unix-epoch")
      .isEqualTo(DancerInvolvement.NEVER);
  }

  @Test
  @DisplayName("The timestamp of the last sent mail of a newly created instance should be the unix-epoch")
  public void testNewInvolvementHasUnixEpochAsLastMailSent()
  {
    // When
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());

    // Then
    assertThat(involvement.getLastMailSent())
      .describedAs("The timestamp of the last sent mail should equal the unix-epoch")
      .isEqualTo(DancerInvolvement.NEVER);
  }

  @Test
  @DisplayName("A newly created instance should have no unseen chat-messages")
  public void testNewInvolvementHasNoUnseenChatMessages()
  {
    // When
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());

    // Then
    assertThat(involvement.getUnseenMessages())
      .describedAs("There should not be any unseen chat-messages")
      .isEmpty();
  }

  @Test
  @DisplayName("If the ID of an unread chat-message is added, it should be added to the unseen messages")
  public void testAddUnreadChatMessageIsAddedToUnseenMessages()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    UUID messageId = UUID.randomUUID();
    ZonedDateTime before = involvement.getLastInvolvement();

    // When
    involvement.addUnreadChatMessage(messageId);

    // Then
    assertThat(involvement.getUnseenMessages())
      .describedAs("The set of unseen messages should contain the added chat-message-ID")
      .contains(messageId);
  }

  @Test
  @DisplayName("If the ID of an unread chat-message is added, it should not change the timestamp of the last involvement")
  public void testAddUnreadChatMessageShouldNotChangeLastInvolvement()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    UUID messageId = UUID.randomUUID();
    ZonedDateTime before = involvement.getLastInvolvement();

    // When
    involvement.addUnreadChatMessage(messageId);

    // Then
    assertThat(involvement.getLastInvolvement())
      .describedAs("The last involvement should not have changed")
      .isEqualTo(before);
  }

  @Test
  @DisplayName("If a timestamp for the last login is set, the timestamp of the last involvement should reflect it")
  public void testSetLastLoginShouldBeReflectedInLastInvolvement()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    UUID messageId = UUID.randomUUID();
    ZonedDateTime before = involvement.getLastInvolvement();
    involvement.addUnreadChatMessage(messageId);
    ZonedDateTime timestampLastLogin = before.plusHours(3);

    // When
    involvement.setLastLogin(timestampLastLogin);

    // Then
    assertThat(involvement.getLastInvolvement())
      .describedAs("The last involvement should reflect the last login")
      .isEqualTo(timestampLastLogin);
  }

  @Test
  @DisplayName("If a timestamp for the last login is set, all messages should be considered as seen")
  public void testSetLastLoginMarksMessagesAsSeen()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    UUID messageId = UUID.randomUUID();
    ZonedDateTime before = involvement.getLastInvolvement();
    involvement.addUnreadChatMessage(messageId);
    ZonedDateTime timestampLastLogin = before.plusHours(3);

    // When
    involvement.setLastLogin(timestampLastLogin);

    // Then
    assertThat(involvement.getUnseenMessages())
      .describedAs("The remembered unseen messages should be cleard")
      .isEmpty();
  }

  @Test
  @DisplayName("If a timestamp for the last login is set, the timestamp for the last mail sent is reset to the unix-epoch")
  public void testSetLastLoginResetsLastMailSent()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    ZonedDateTime before = involvement.getLastInvolvement();
    ZonedDateTime timestampLastMailSent = before.plusHours(1);
    involvement.setLastMailSent(timestampLastMailSent);
    ZonedDateTime timestampLastLogin = before.plusHours(3);

    // When
    involvement.setLastLogin(timestampLastLogin);

    // Then
    assertThat(involvement.getLastMailSent())
      .describedAs("The timestamp for the last mail sent should equal the unix-epoch")
      .isEqualTo(DancerInvolvement.NEVER);
  }

  @Test
  @DisplayName("If the chat-messages are marked as seen, the according timestamp is remembered as last involvement")
  public void testMarkChatMessagesAsSeenShouldBeRememberedAsLastInvolvement()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    ZonedDateTime before = involvement.getLastInvolvement();
    ZonedDateTime timestampMessageRead = before.plusHours(3);

    // When
    involvement.markChatMessagesAsSeen(timestampMessageRead);

    // Then
    assertThat(involvement.getLastInvolvement())
      .describedAs("Last involvement should reflect the time the message was read")
      .isEqualTo(timestampMessageRead);
  }

  @Test
  @DisplayName("If the chat-messages are marked as seen, the set of unseen messages should be cleared")
  public void testMarkChatMessagesAsSeenShouldClearTheUnseenMessages()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    UUID messageId = UUID.randomUUID();
    ZonedDateTime before = involvement.getLastInvolvement();
    involvement.addUnreadChatMessage(messageId);
    ZonedDateTime timestampMessageRead = before.plusHours(3);

    // When
    involvement.markChatMessagesAsSeen(timestampMessageRead);

    // Then
    assertThat(involvement.getUnseenMessages())
      .describedAs("The remembered unseen messages should be cleard")
      .isEmpty();
  }

  @Test
  @DisplayName("If the chat-messages are marked as seen, the timestamp for the last mail sent is reset to the unix-epoch")
  public void testMarkChatMessagesAsSeenResetsLastMailSent()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    ZonedDateTime before = involvement.getLastInvolvement();
    ZonedDateTime timestampLastMailSent = before.plusHours(1);
    involvement.setLastMailSent(timestampLastMailSent);
    ZonedDateTime timestampMessageRead = before.plusHours(3);

    // When
    involvement.markChatMessagesAsSeen(timestampMessageRead);

    // Then
    assertThat(involvement.getLastMailSent())
      .describedAs("The timestamp for the last mail sent should equal the unix-epoch")
      .isEqualTo(DancerInvolvement.NEVER);
  }

  @Test
  @DisplayName("If a timestamp for the last sent mail is set, it should be stored")
  public void testSetLastMailSentIsStored()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    UUID messageId = UUID.randomUUID();
    ZonedDateTime before = involvement.getLastInvolvement();
    involvement.addUnreadChatMessage(messageId);
    ZonedDateTime timestampLastMailSent = before.plusHours(3);

    // When
    involvement.setLastMailSent(timestampLastMailSent);

    // Then
    assertThat(involvement.getLastMailSent())
      .describedAs("The timestamp for the last sent mail should have been stored")
      .isEqualTo(timestampLastMailSent);
  }

  @Test
  @DisplayName("If a timestamp for the last sent mail is set, it should not change the last involvement")
  public void testSetLastMailSentDoesNotChangeTheLastInvolvement()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    ZonedDateTime before = involvement.getLastInvolvement();
    ZonedDateTime timestampLastMailSent = before.plusHours(3);

    // When
    involvement.setLastMailSent(timestampLastMailSent);

    // Then
    assertThat(involvement.getLastInvolvement())
      .describedAs("The last involvement should not have been changed")
      .isEqualTo(before);
  }

  @Test
  @DisplayName("If a timestamp for the last sent mail is set, the unseen chant-messages should not be cleared")
  public void testSetLastMailSentDoesNotClearUnseenMessages()
  {
    // Given
    DancerInvolvement involvement = new DancerInvolvement(UUID.randomUUID());
    UUID messageId = UUID.randomUUID();
    ZonedDateTime before = involvement.getLastInvolvement();
    involvement.addUnreadChatMessage(messageId);
    ZonedDateTime timestampLastMailSent = before.plusHours(3);

    // When
    involvement.setLastMailSent(timestampLastMailSent);

    // Then
    assertThat(involvement.getUnseenMessages())
      .describedAs("The remembered unseen messages should not be cleared")
      .isNotEmpty();
  }
}
