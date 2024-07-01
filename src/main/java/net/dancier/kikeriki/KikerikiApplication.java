package net.dancier.kikeriki;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@Slf4j
public class KikerikiApplication
{
  public static void main(String[] args)
  {
    SpringApplication.run(KikerikiApplication.class, args);
  }
}

