package net.dancier.kikeriki;

import net.dancier.kikeriki.involvement.DummyInvolvementStrategy;
import net.dancier.kikeriki.kafka.InvolveDancersMessageHandler;
import net.dancier.kikeriki.kafka.KikerikiConsumer;
import net.dancier.kikeriki.kafka.MessageHandler;
import net.dancier.kikeriki.messages.Message;
import net.dancier.kikeriki.model.InvolvementStrategy;
import net.dancier.kikeriki.model.KikerikiService;
import net.dancier.kikeriki.model.KikerikiState;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;


@Configuration
@EnableConfigurationProperties({ KafkaProperties.class, KikerikiApplicationProperties.class })
public class KikerikiApplicationConfiguration
{
  @Bean
  public KikerikiService kikerikiService(
    KikerikiApplicationProperties properties,
    InvolvementStrategy involvementStrategy)
  {
    return new KikerikiService(
      properties.getInvolveDancerAfter(),
      properties.getInvolvementCheckInterval(),
      properties.getReinvolvementInterval(),
      involvementStrategy);
  }

  @Bean
  public InvolvementStrategy involvementStrategy()
  {
    return new DummyInvolvementStrategy();
  }

  @Bean
  public MessageHandler messageHandler(
    KikerikiApplicationProperties properties,
    KikerikiService kikerikiService)
  {
    return new InvolveDancersMessageHandler(
      () -> new KikerikiState(),
      properties.getNumPartitions(),
      kikerikiService);
  }

  @Bean
  public KikerikiConsumer kikerikiConsumer(
      Consumer<String, Message> kafkaConsumer,
      MessageHandler messageHandler,
      KafkaProperties kafkaProperties,
      KikerikiApplicationProperties kikerikiApplicationProperties)
  {
    return
        new KikerikiConsumer(
            kafkaProperties.getClientId(),
            kikerikiApplicationProperties.getTopic(),
            kafkaConsumer,
            messageHandler);
  }

  @Bean
  public Consumer<?, ?> kafkaConsumer(ConsumerFactory<?, ?> factory)
  {
    return factory.createConsumer();
  }
}
