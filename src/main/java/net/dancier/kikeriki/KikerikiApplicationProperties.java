package net.dancier.kikeriki;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;


@ConfigurationProperties(prefix = "dancier.kikeriki")
@Validated
@Getter
@Setter
public class KikerikiApplicationProperties
{
  @NotNull
  @NotEmpty
  private String topic;

  @Positive
  private Integer numPartitions;

  @NotNull
  private Duration involveDancerAfter;

  @NotNull
  private Duration involvementCheckInterval;

  @NotNull
  private Duration reinvolvementInterval;
}
