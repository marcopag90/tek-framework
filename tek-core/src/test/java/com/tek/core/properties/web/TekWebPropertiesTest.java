package com.tek.core.properties.web;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tek.core.properties.TekCoreProperties;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class})
@EnableConfigurationProperties(TekCoreProperties.class)
@TestPropertySource("classpath:web-custom.properties")
@TestMethodOrder(OrderAnnotation.class)
class TekWebPropertiesTest {

  @Autowired
  private TekCoreProperties coreCustomProperties;

  @Test
  @Order(1)
  void defaultValues() {
    final var properties = new TekWebProperties();
    assertAll(
        () -> assertTrue(properties.isEnabled()),
        () -> assertEquals("index.html", properties.getIndexPage())
    );
  }

  @Test
  @Order(2)
  void customValues() {
    final var properties = coreCustomProperties.getWebConfiguration();
    assertNotNull(properties);
    assertAll(
        () -> assertFalse(properties.isEnabled()),
        () -> assertEquals("index-custom.html", properties.getIndexPage())
    );
  }

}
