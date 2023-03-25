package net.dancier.kikeriki.model;

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
  @DisplayName("A newly created KikerikiState should not contain any DancerState")
  public void testNewKikerikiStateShouldNotContainDancerState()
  {
    // Given

    // When
    KikerikiState state = new KikerikiState();

    // Then
    assertThat(state.getDancerState())
      .describedAs("There should not exist any DancerState yet")
      .isEmpty();
  }

  @Test
  @DisplayName("getOrCreateDancerState() creates a DancerState, if it does not exist")
  public void testGetOrCreateDancerStateCreatesDancerState()
  {
    // Given
    KikerikiState kikerikiState = new KikerikiState();

    // When
    DancerState created = kikerikiState.getDancerState(UUID.randomUUID());

    // Then
    assertThat(
      kikerikiState
        .getDancerState()
        .filter(dancerState -> dancerState.equals(created))
        .count())
      .describedAs("It should exist exactly one DancerState with the UUID")
      .isEqualTo(1);
  }

  @Test
  @DisplayName("getDancerState() does not create a DancerState, if it already exists")
  public void testGetDancerStateDoesNotCreateDancerStateIfExists()
  {
    // Given
    KikerikiState kikerikiState = new KikerikiState();
    UUID uuid = UUID.randomUUID();

    // When
    kikerikiState.getDancerState(uuid);

    // Then
    assertThat(
      kikerikiState
        .getDancerState()
        .filter(dancerStateement -> dancerStateement.getDancerId().equals(uuid))
        .count())
      .describedAs("It should exist exactly one DancerState with the UUID")
      .isEqualTo(1);
  }

  @Test
  @DisplayName("getDancerState() retrieves an existing DancerState")
  public void testGetDancerStateRetreivesExistingDancerState()
  {
    // Given
    KikerikiState kikerikiState = new KikerikiState();
    UUID uuid = UUID.randomUUID();
    DancerState created = kikerikiState.getDancerState(uuid);

    // When
    DancerState retreived = kikerikiState.getDancerState(uuid);

    // Then
    assertThat(retreived)
      .describedAs("The retrieved DancerState should equal the one that was created before.")
      .isEqualTo(created);
  }

  @Test
  public void testMiniIntegrationExample()
  {
    KikerikiState kikerikiState = new KikerikiState();
    DancerState dancerState;


    dancerState = kikerikiState.handle(read(loginMessage, MessageLogin.class));
    assertThat(dancerState.getLastInvolvement().map(zdt -> zdt.toInstant()))
      .describedAs("Unexpected last involvement")
      .contains(ZonedDateTime.parse("2021-12-31T00:00:00+01:00[Europe/Berlin]").toInstant());

    dancerState = kikerikiState.handle(read(chatMessageNewMessage, MessageChat.class));
    assertThat(dancerState.getUnseenMessages())
      .describedAs("Unread chat-message is missing")
      .contains(UUID.fromString("a58ed763-728c-9355-b339-3db21adc15a3"));

    dancerState = kikerikiState.handle(read(chatMessageMessageRead, MessageChat.class));
    assertThat(dancerState.getLastInvolvement().map(zdt -> zdt.toInstant()))
      .describedAs("Unexpected last involvement")
      .contains(ZonedDateTime.parse("2022-01-03T00:00:00+01:00[Europe/Berlin]").toInstant());
    assertThat(dancerState.getUnseenMessages())
      .describedAs("Unread chat-message should be cleared")
      .isEmpty();

    kikerikiState.handle(read(mailSentMessage, MessageMailSent.class));
    assertThat(dancerState.getLastInvolvement().map(zdt -> zdt.toInstant()))
      .describedAs("Unexpected last involvement")
      .contains(ZonedDateTime.parse("2022-01-03T00:00:00+01:00[Europe/Berlin]").toInstant());
    assertThat(dancerState.getLastMailSent().map(zdt -> zdt.toInstant()))
      .describedAs("Unexpected timestamp for last sent mail")
      .contains(ZonedDateTime.parse("2022-01-04T00:00:00+01:00[Europe/Berlin]").toInstant());
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