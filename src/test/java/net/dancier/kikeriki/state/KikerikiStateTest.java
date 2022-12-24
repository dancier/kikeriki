package net.dancier.kikeriki.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.dancier.kikeriki.messages.MessageChat;
import net.dancier.kikeriki.messages.MessageLogin;
import net.dancier.kikeriki.messages.MessageMailSent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@ExtendWith(SpringExtension.class)
class KikerikiStateTest
{
  ObjectMapper mapper;

  @Value("classpath:messages/login.json")
  Resource loginMessage;
  @Value("classpath:messages/chat.json")
  Resource chatMessageNewMessage;
  @Value("classpath:messages/chat-with-unknown-field.json")
  Resource chatMessageMessageRead;
  @Value("classpath:messages/mail-sent.json")
  Resource mailSentMessage;

  /**
   * Sending mails to the customer should happen exactly when:
   * <p>
   * To be defined ;-)
   */

  @BeforeEach
  public void init()
  {
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
  }

  @Test
  @DisplayName("A newly created KikerikiState should not contain any DancerInvolvement's")
  public void testNewKikerikyStateShouldNotContainDancerInvolvements()
  {
    // Given

    // When
    KikerikiState state = new KikerikiState();

    // Then
    assertThat(state.getDancerInvolvements())
      .describedAs("There should not exist any DancerInvolvement yet")
      .isEmpty();
  }

  @Test
  @DisplayName("getOrCreateDancerInvolvement() creates a DancerInvolvement, if it does not exist")
  public void testGetOrCreateDancerInvolvementCreatesDancerInvolvement()
  {
    // Given
    KikerikiState state = new KikerikiState();

    // When
    DancerInvolvement created = state.getDancerInvolvement(UUID.randomUUID());

    // Then
    assertThat(
      state
        .getDancerInvolvements()
        .filter(involvement -> involvement.equals(created))
        .count())
      .describedAs("It should exist exactly one DancerInvolvement with the UUID")
      .isEqualTo(1);
  }

  @Test
  @DisplayName("getOrCreateDancerInvolvement() does not create a DancerInvolvement, if it already exists")
  public void testGetOrCreateDancerInvolvementDoesNotCreateDancerInvolvementIfExists()
  {
    // Given
    KikerikiState state = new KikerikiState();
    UUID uuid = UUID.randomUUID();

    // When
    state.getDancerInvolvement(uuid);

    // Then
    assertThat(
      state
        .getDancerInvolvements()
        .filter(involvement -> involvement.getDancerId().equals(uuid))
        .count())
      .describedAs("It should exist exactly one DancerInvolvement with the UUID")
      .isEqualTo(1);
  }

  @Test
  @DisplayName("getOrCreateDancerInvolvement() retreives an existing DancerInvolvement")
  public void testGetOrCreateDancerInvolvementRetreivesExistingDancerInvolvement()
  {
    // Given
    KikerikiState state = new KikerikiState();
    UUID uuid = UUID.randomUUID();
    DancerInvolvement created = state.getDancerInvolvement(uuid);

    // When
    DancerInvolvement retreived = state.getDancerInvolvement(uuid);

    // Then
    assertThat(retreived)
      .describedAs("The retreived DancerInvolvement should equal the one that was created before.")
      .isEqualTo(created);
  }

  @Test
  public void testMiniIntegrationExample()
  {
    KikerikiState state = new KikerikiState();
    DancerInvolvement involvement;


    involvement = state.handle(read(loginMessage, MessageLogin.class));
    assertThat(involvement.getLastInvolvement())
      .describedAs("Unexpected last involvement")
      .isEqualTo(ZonedDateTime.parse("2021-12-31T00:00:00+01:00[Europe/Berlin]"));

    involvement = state.handle(read(chatMessageNewMessage, MessageChat.class));
    assertThat(involvement.getUnseenMessages())
      .describedAs("Unread chat-message is missing")
      .contains(UUID.fromString("a58ed763-728c-9355-b339-3db21adc15a3"));

    involvement = state.handle(read(chatMessageMessageRead, MessageChat.class));
    assertThat(involvement.getLastInvolvement())
      .describedAs("Unexpected last involvement")
      .isEqualTo(ZonedDateTime.parse("2022-01-03T00:00:00+01:00[Europe/Berlin]"));
    assertThat(involvement.getUnseenMessages())
      .describedAs("Unread chat-message should be cleared")
      .isEmpty();

    state.handle(read(mailSentMessage, MessageMailSent.class));
    assertThat(involvement.getLastInvolvement())
      .describedAs("Unexpected last involvement")
      .isEqualTo(ZonedDateTime.parse("2022-01-03T00:00:00+01:00[Europe/Berlin]"));
    assertThat(involvement.getLastMailSent())
      .describedAs("Unexpected timestamp for last sent mail")
      .isEqualTo(ZonedDateTime.parse("2022-01-04T00:00:00+01:00[Europe/Berlin]"));
  }


  <T> T read(Resource resource, Class<T> valueType)
  {
    try
    {
      URI uri = resource.getURI();
      File file = ResourceUtils.getFile(uri);
      return mapper.readValue(new FileInputStream(file), valueType);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
}