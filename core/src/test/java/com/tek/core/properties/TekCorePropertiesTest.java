package com.tek.core.properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TekCorePropertiesTest {

  @Test
  void defaultValues() {
    val properties = new TekCoreProperties();
    Assertions.assertAll(
        () -> assertNotNull(properties.getCors()),
        () -> assertNotNull(properties.getLocale()),
        () -> assertNotNull(properties.getFile()),
        () -> assertNotNull(properties.getFile()),
        () -> assertNotNull(properties.getMail()),
        () -> assertNotNull(properties.getScheduler())
    );
  }
}
