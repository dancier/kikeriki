package net.dancier.kikeriki;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@ContextConfiguration(initializers = AbstractPostgreSQLEnabledTest.DockerPostgreSQLDataSourceInitializer.class)
public class AbstractPostgreSQLEnabledTest {

  private static final Logger log = LoggerFactory.getLogger(AbstractPostgreSQLEnabledTest.class);

  static KafkaContainer kafkaContainer = new KafkaContainer(
    DockerImageName.parse("confluentinc/cp-kafka:7.3.3")
  );

  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    @BeforeAll
    static void beforeAll() {
      postgreSQLContainer.start();
      kafkaContainer.start();
      log.info("Started PostgreSQLContainer...");
    }

    @AfterAll
    static void afterAll() {
      postgreSQLContainer.stop();
      kafkaContainer.stop();
      log.info("Stopped PostgreSQLContainer...");
    }

    public static class DockerPostgreSQLDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    configurableApplicationContext,
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            );
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
              configurableApplicationContext,
              "spring.kafka.bootstrap-servers=" + kafkaContainer.getBootstrapServers()
            );
        }
    }
}
