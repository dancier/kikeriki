package net.dancier.kikeriki.adapter.in.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dancier.kikeriki.NeededInfrastructureBaseTestClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.BDDAssertions.then;

public class DeserializationTest extends NeededInfrastructureBaseTestClass {

  @Autowired
  ObjectMapper objectMapper;

  @Test
  public void canDeserialize() throws JsonProcessingException {
    String testSerializedObject = """
      {
        "text": "Hallo",
        "chatId": "5cb1f9b2-8e03-4714-a50e-08c681d8fd73",
        "authorId": "6d99cb73-a67b-4660-9c05-1f79333f30c5",
        "createdAt": "2024-01-04T19:01:21.75405921Z",
        "messageId": "8376b7b1-f91d-4f91-9101-3822250f0db5",
        "participantIds": [
          "0b17dc39-645f-41b9-80e7-cb2ea7a6fe5b",
          "6d99cb73-a67b-4660-9c05-1f79333f30c5"
        ]
      }
      """;

    MessagePostedEventDto messagePostedEventDto = objectMapper.readValue(testSerializedObject, MessagePostedEventDto.class);

    then(messagePostedEventDto).isNotNull();
    then(messagePostedEventDto.participantIds).isNotEmpty();
    then(messagePostedEventDto.authorId).isNotBlank();
    then(messagePostedEventDto.createdAt).isNotNull();
    then(messagePostedEventDto.messageId).isNotNull();
  }

}
