package net.dancier.kikeriki;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


@ConfigurationProperties(prefix = "dancier.kikeriki")
@Validated
@Getter
@Setter
public class KikerikiApplicationProperties
{
  @NotNull
  @NotEmpty
  private String topic;
}
