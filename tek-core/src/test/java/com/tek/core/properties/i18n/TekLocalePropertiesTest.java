package com.tek.core.properties.i18n;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.tek.core.properties.TekCoreProperties;
import com.tek.core.properties.i18n.TekLocaleProperties.TekLocaleType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = TekCoreProperties.class)
@TestPropertySource("classpath:i18n-custom.properties")
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class TekLocalePropertiesTest {

  @Autowired
  private TekCoreProperties coreCustomProperties;

  @BeforeAll
  @Test
  void test_setup() {
    Assertions.assertAll(
        () -> assertNotNull(coreCustomProperties)
    );
  }

  @Test
  @Order(1)
  void defaultValues() {
    final var properties = new TekCoreProperties().getLocaleConfiguration();
    assertNotNull(properties);
    assertAll(
        () -> assertEquals(TekLocaleType.SESSION, properties.getType()),
        () -> assertEquals("locale", properties.getCookieName()),
        () -> assertEquals(-1, properties.getCookieMaxAge())
    );
  }

  @Test
  @Order(2)
  void customValues() {
    final var properties = coreCustomProperties.getLocaleConfiguration();
    assertNotNull(properties);
    assertAll(
        () -> assertEquals(TekLocaleType.COOKIE, properties.getType()),
        () -> assertEquals("custom-cookie", properties.getCookieName()),
        () -> assertEquals(1000, properties.getCookieMaxAge())
    );
  }

}
