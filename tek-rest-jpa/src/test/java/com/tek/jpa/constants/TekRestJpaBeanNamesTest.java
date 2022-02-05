package com.tek.jpa.constants;

import static com.tek.jpa.constants.TekRestJpaBeanNames.TEK_REST_JPA_CONFIGURATION;
import static com.tek.jpa.constants.TekRestJpaBeanNames.TEK_REST_JPA_HIBERNATE_5_MODULE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TekRestJpaBeanNamesTest {

  @Test
  void testConstantValues() {
    Assertions.assertAll(
        () -> assertEquals(
            "com.tek.jpa.config.TekRestJpaModuleConfiguration",
            TEK_REST_JPA_CONFIGURATION
        ),
        () -> assertEquals(
            "com.tek.jpa.config.TekRestJpaModuleConfiguration.hibernate5Module",
            TEK_REST_JPA_HIBERNATE_5_MODULE
        )
    );
  }
}
