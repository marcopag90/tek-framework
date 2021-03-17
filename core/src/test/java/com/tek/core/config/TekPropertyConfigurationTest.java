package com.tek.core.config;


import static com.tek.core.constants.TekCoreBeanConstants.TEK_CORE_PROP_PLACEHOLDER_CONF_BEAN;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class TekPropertyConfigurationTest {

  private final ApplicationContextRunner context = new ApplicationContextRunner();

  @Test
  void testPlaceholderConfigurer() {
    this.context.withUserConfiguration(TekPropertyConfiguration.class)
        .run(context -> assertThat(context).hasBean(TEK_CORE_PROP_PLACEHOLDER_CONF_BEAN));
  }
}
