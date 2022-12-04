package net.dancier.kikeriki;

import net.dancier.kikeriki.messages.Message;
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
