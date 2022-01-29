package com.tek.core.properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TekCorePropertiesTest {

  @Test
  void defaultValues() {
    final var properties = new TekCoreProperties();
    Assertions.assertAll(
        () -> assertNotNull(properties.getLocaleConfiguration()),
        () -> assertNotNull(properties.getFileConfiguration()),
        () -> assertNotNull(properties.getFileConfiguration()),
        () -> assertNotNull(properties.getMailConfiguration())
    );
  }
}
