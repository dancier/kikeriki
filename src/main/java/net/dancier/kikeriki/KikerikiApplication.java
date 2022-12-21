package net.dancier.kikeriki;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;


@SpringBootApplication
@Slf4j
public class KikerikiApplication implements ApplicationRunner
{
  @Autowired
  ThreadPoolTaskExecutor taskExecutor;
  @Autowired
  Consumer<?, ?> kafkaConsumer;
  @Autowired
  KikerikiConsumer kikerikiConsumer;
  @Autowired
  ConfigurableApplicationContext context;

  @Autowired
  GitProperties gitProperties;

  ListenableFuture<Integer> consumerJob;

  @Override
  public void run(ApplicationArguments args)
  {
    log.info("Starting KikerikiConsumer");
    consumerJob = taskExecutor.submitListenable(kikerikiConsumer);
    consumerJob.addCallback(
      exitStatus ->
      {
        log.info("KikerikiConsumer exited normally, exit-status: {}", exitStatus);
        SpringApplication.exit(context, () -> exitStatus);
      },
      t ->
      {
        log.error("KikerikiConsumer exited abnormally!", t);
        SpringApplication.exit(context, () -> 2);
      });
  }

  @PreDestroy
  public void shutdown() throws ExecutionException, InterruptedException
  {
    log.info("Signaling KikerikiConsumer to quit its work");
    kafkaConsumer.wakeup();
    log.info("Waiting for KikerikiConsumer to finish its work");
    consumerJob.get();
    log.info("KikerikiConsumer finished its work");
  }


  public static void main(String[] args)
  {
    SpringApplication.run(KikerikiApplication.class, args);
  }
}

