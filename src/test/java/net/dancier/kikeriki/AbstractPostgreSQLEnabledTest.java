package net.dancier.kikeriki;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;

@ContextConfiguration(initializers = AbstractPostgreSQLEnabledTest.DockerPostgreSQLDataSourceInitializer.class)
public class AbstractPostgreSQLEnabledTest {

  private static final Logger log = LoggerFactory.getLogger(AbstractPostgreSQLEnabledTest.class);

  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    @BeforeAll
    static void beforeAll() {
      postgreSQLContainer.start();
      log.info("Started PostgreSQLContainer...");
    }

    @AfterAll
    static void afterAll() {
      postgreSQLContainer.stop();
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
        }
    }
}
