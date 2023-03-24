package net.dancier.kikeriki;

import net.dancier.kikeriki.kafka.InvolveDancersMessageHandler;
import net.dancier.kikeriki.kafka.KikerikiConsumer;
import net.dancier.kikeriki.kafka.MessageHandler;
import net.dancier.kikeriki.messages.Message;
import net.dancier.kikeriki.state.KikerikiState;
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
  public DancerInvolver involver(
    KikerikiApplicationProperties properties)
  {
    return new DancerInvolver(
      properties.getInvolveDancerAfter(),
      properties.getInvolvementCheckInterval(),
      properties.getReinvolvementInterval());
  }

  @Bean
  public MessageHandler messageHandler(
    KikerikiApplicationProperties properties,
    DancerInvolver involver)
  {
    return new InvolveDancersMessageHandler(
      () -> new KikerikiState(),
      properties.getNumPartitions(),
      involver);
  }

  @Bean
  public KikerikiConsumer kikerikiConsumer(
      Consumer<String, Message> kafkaConsumer,
      MessageHandler messageHandler,
      KafkaProperties kafkaProperties,
      KikerikiApplicationProperties applicationProperties)
  {
    return
        new KikerikiConsumer(
            kafkaProperties.getClientId(),
            applicationProperties.getTopic(),
            kafkaConsumer,
            messageHandler);
  }

  @Bean
  public Consumer<?, ?> kafkaConsumer(ConsumerFactory<?, ?> factory)
  {
    return factory.createConsumer();
  }
}
