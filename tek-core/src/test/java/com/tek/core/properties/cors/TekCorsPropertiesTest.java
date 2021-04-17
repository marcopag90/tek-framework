package com.tek.core.properties.cors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tek.core.properties.TekCoreProperties;
import lombok.val;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = TekCoreProperties.class)
@TestPropertySource("classpath:/cors-custom.properties")
@TestMethodOrder(OrderAnnotation.class)
class TekCorsPropertiesTest {

  @Autowired
  private TekCoreProperties coreCustomProperties;

  @Test
  @Order(1)
  void defaultValues() {
    val properties = new TekCoreProperties().getCors();
    assertNotNull(properties);
    String allowedOrigin = properties.getAllowedOrigin();
    val allowedCredentials = properties.getAllowedCredentials();
    val allowedMethodsProps = properties.getAllowedMethods();
    val allowedHeadersProps = properties.getAllowedHeaders();
    assertAll(
        () -> assertNotNull(allowedOrigin),
        () -> assertNotNull(allowedCredentials),
        () -> assertNotNull(allowedMethodsProps),
        () -> assertNotNull(allowedHeadersProps)
    );
    val allowedMethods = String.join(",", properties.getAllowedMethods());
    val expectedMethods = "GET,HEAD,POST,PUT,PATCH,DELETE,OPTIONS,TRACE";
    val allowedHeaders = String.join(",", properties.getAllowedHeaders());
    val expectedHeaders = "x-requested-with,authorization,Content-Type,Authorization,credential,X-XSRF-TOKEN";
    assertAll(
        () -> assertEquals("http://localhost:4200", allowedOrigin),
        () -> assertTrue(allowedCredentials),
        () -> assertEquals(expectedMethods, allowedMethods),
        () -> assertEquals(expectedHeaders, allowedHeaders)
    );
  }

  @Test
  @Order(2)
  void customValues() {
    TekCorsProperties properties = coreCustomProperties.getCors();
    assertNotNull(properties);
    String allowedOrigin = properties.getAllowedOrigin();
    val allowedCredentials = properties.getAllowedCredentials();
    val allowedMethodsProps = properties.getAllowedMethods();
    val allowedHeadersProps = properties.getAllowedHeaders();
    assertAll(
        () -> assertNotNull(allowedOrigin),
        () -> assertNotNull(allowedCredentials),
        () -> assertNotNull(allowedMethodsProps),
        () -> assertNotNull(allowedHeadersProps)
    );
    val allowedMethods = String.join(",", properties.getAllowedMethods());
    val expectedMethods = "GET,POST,PATCH,PUT,DELETE";
    val allowedHeaders = String.join(",", properties.getAllowedHeaders());
    val expectedHeaders = "x-requested-with,authorization,Content-Type,Authorization,credential";
    assertAll(
        () -> assertEquals("http://localhost:4201", allowedOrigin),
        () -> assertFalse(allowedCredentials),
        () -> assertEquals(expectedMethods, allowedMethods),
        () -> assertEquals(expectedHeaders, allowedHeaders)
    );
  }
}
