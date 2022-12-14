package net.dancier.kikeriki;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dancier.kikeriki.messages.*;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.Callable;


@Slf4j
@RequiredArgsConstructor
public class KikerikiConsumer implements Callable<Integer>
{
  private final String id;
  private final String topic;
  private final Consumer<String, Message> consumer;
  private final MessageHandler messageHandler;

  private long consumed = 0;


  @Override
  public Integer call()
  {
    try
    {
      log.info("{} - Subscribing to topic {}", id, topic);
      KikerikiRebalanceListener rebalanceListener =
        new KikerikiRebalanceListener(consumer, messageHandler);
      consumer.subscribe(Arrays.asList(topic), rebalanceListener);

      while (true)
      {
        ConsumerRecords<String, Message> records =
            consumer.poll(Duration.ofSeconds(1));

        log.info("{} - Received {} messages", id, records.count());
        for (ConsumerRecord<String, Message> record : records)
        {
          handleRecord(
            record.topic(),
            record.partition(),
            record.offset(),
            record.key(),
            record.value());
        }
      }
    }
    catch(WakeupException e)
    {
      log.info("{} - Consumer was signaled to finish its work", id);
      return 0;
    }
    catch(Exception e)
    {
      log.error("{} - Unexpected error: {}, unsubscribing!", id, e.toString());
      consumer.unsubscribe();
      return 1;
    }
    finally
    {
      log.info("{} - Closing the KafkaConsumer", id);
      consumer.close();
      log.info("{}: Consumed {} messages in total, exiting!", id, consumed);
    }
  }

  private void handleRecord(
    String topic,
    Integer partition,
    Long offset,
    String key,
    Message message)
  {
    consumed++;
    log.debug("{} - {}: {}/{} - {}={}", id, offset, topic, partition, key, message);
    messageHandler.handle(partition, offset, message);
  }
}
