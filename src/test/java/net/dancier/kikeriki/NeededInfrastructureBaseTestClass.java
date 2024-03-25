package net.dancier.kikeriki;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@DirtiesContext
@Testcontainers
@ContextConfiguration(initializers = NeededInfrastructureBaseTestClass.DataSourceInitializer.class)
public class NeededInfrastructureBaseTestClass {

  private static final Logger log = LoggerFactory.getLogger(NeededInfrastructureBaseTestClass.class);


  @Container
  final static KafkaContainer kafkaContainer = new KafkaContainer(
    DockerImageName.parse("confluentinc/cp-kafka:7.3.3")
  );

  @Container
  final static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine");
/**    @BeforeAll
    static void beforeAll() {
      postgreSQLContainer.start();
      kafkaContainer.start();
      log.info("Started needed Container...");
    }
    @AfterAll
    static void afterAll() {
      postgreSQLContainer.stop();
      kafkaContainer.stop();
      log.info("Stopped needed Container...");
    }
 **/

    public static class DataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

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
