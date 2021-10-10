package com.tek.core.properties.swagger;

import static com.tek.core.properties.swagger.TekSwaggerProperties.DEFAULT_DESCRIPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.tek.core.properties.TekCoreProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ReflectionUtils;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = TekCoreProperties.class)
@TestPropertySource("classpath:swagger-custom.properties")
@TestMethodOrder(OrderAnnotation.class)
class TekSwaggerPropertiesTest {

  @Autowired
  private TekCoreProperties coreCustomProperties;

  @Test
  @Order(1)
  void defaultValues() {
    final var properties = new TekCoreProperties().getSwaggerConfiguration();
    assertNotNull(properties);
    Assertions.assertAll(
        () -> assertEquals(DEFAULT_DESCRIPTION, properties.getDescription()),
        () -> assertNull(properties.getTermOfServiceUrl()),
        () -> assertNull(properties.getContactName()),
        () -> assertNull(properties.getContactUrl()),
        () -> assertNull(properties.getContactEmail()),
        () -> assertNull(properties.getLicense()),
        () -> assertNull(properties.getLicenseUrl())
    );
  }

  @Test
  @Order(2)
  void customValues() {
    final var properties = coreCustomProperties.getSwaggerConfiguration();
    assertNotNull(properties);
    Assertions.assertAll(
        () -> assertEquals(fieldByName("description"), properties.getDescription()),
        () -> assertEquals(fieldByName("termOfServiceUrl"), properties.getTermOfServiceUrl()),
        () -> assertEquals(fieldByName("contactName"), properties.getContactName()),
        () -> assertEquals(fieldByName("contactUrl"), properties.getContactUrl()),
        () -> assertEquals(fieldByName("contactEmail"), properties.getContactEmail()),
        () -> assertEquals(fieldByName("license"), properties.getLicense()),
        () -> assertEquals(fieldByName("licenseUrl"), properties.getLicenseUrl())
    );
  }

  private String fieldByName(String name) {
    final var field = ReflectionUtils.findField(TekSwaggerProperties.class, name);
    return field == null ? null : field.getName();
  }
}
