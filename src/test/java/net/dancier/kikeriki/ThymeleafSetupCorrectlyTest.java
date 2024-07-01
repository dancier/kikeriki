package net.dancier.kikeriki;

import net.dancier.kikeriki.application.CheckAndSendService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ThymeleafSetupCorrectlyTest extends NeededInfrastructureBaseTestClass {

  @Autowired
  TemplateEngine templateEngine;

  static Context context;

  @BeforeAll
  static void init() {
    Map<String, Object> map = new HashMap<>();
    map.put("unread_messages", 10);
    context = new Context(Locale.GERMANY, map);
  }

  @Test
  public void test() {
    String result = templateEngine.process(CheckAndSendService.USER_INFO_MAIL, context);
    assertThat(result).isNotBlank();
  }

}
