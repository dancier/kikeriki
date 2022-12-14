package net.dancier.kikeriki;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class KikerikiRebalanceListener implements ConsumerRebalanceListener
{
  private final Consumer consumer;
  private final MessageHandler messageHandler;

  @Override
  public void onPartitionsAssigned(Collection<TopicPartition> partitions)
  {
    log.info("Assigned partitions: {}", partitions);

    Map<TopicPartition, Long> offsets = consumer.endOffsets(partitions);
    offsets.forEach((tp, offset) -> messageHandler.addPartition(tp.partition(), offset));
  }

  @Override
  public void onPartitionsRevoked(Collection<TopicPartition> partitions)
  {
    log.info("Revoked partitions: {}", partitions);
    partitions.forEach(tp -> messageHandler.removePartition(tp.partition()));
  }

  @Override
  public void onPartitionsLost(Collection<TopicPartition> partitions)
  {
    log.warn("Lost partitions: {}", partitions);
  }
}
