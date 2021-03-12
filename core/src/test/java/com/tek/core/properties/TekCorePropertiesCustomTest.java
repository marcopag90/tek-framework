package com.tek.core.properties;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tek.core.TekCoreProperties;
import com.tek.core.properties.file.TekFileProperties;
import com.tek.core.properties.i18n.TekLocaleProperties;
import com.tek.core.properties.i18n.TekLocaleProperties.TekLocaleType;
import java.io.File;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = TekCoreProperties.class)
@TestPropertySource("classpath:core-custom.properties")
class TekCorePropertiesCustomTest {

  @Autowired
  private TekCoreProperties coreProperties;

  @Value("${spring.application.name}")
  private String applicationName;

  @Test
  void testApplicationName() {
    assertEquals("Tek Test Application", applicationName);
  }

  @Test
  void testCorsProperties() {
    TekCorsProperties properties = coreProperties.getCors();
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

  @Test
  void testLocaleProperties() {
    TekLocaleProperties properties = coreProperties.getLocale();
    assertNotNull(properties);
    assertAll(
        () -> assertEquals(TekLocaleType.COOKIE, properties.getType()),
        () -> assertEquals("custom-cookie", properties.getCookieName()),
        () -> assertEquals(1000, properties.getCookieMaxAge())
    );
  }

  @Test
  void testMailProperties() {
    TekMailProperties properties = coreProperties.getMail();
    assertNotNull(properties);
    assertAll(
        () -> assertTrue(properties.isSendErrors()),
        () -> assertTrue(properties.isRealDelivery())
    );
  }

  @Test
  void testSchedulerProperties() {
    TekSchedulerProperties properties = coreProperties.getScheduler();
    assertNotNull(properties);
    assertTrue(properties.getActive());
  }

  @Test
  void testFileTmpProperties() {
    TekFileProperties fileProperties = coreProperties.getFile();
    assertNotNull(fileProperties);
    val tmpProperties = fileProperties.getTmp();
    File tmpDirectory = tmpProperties.getDirectory();
    Integer tmpCleanAfter = tmpProperties.getCleanAfter();
    assertAll(
        () -> assertEquals(new File("tmpDir"), tmpDirectory),
        () -> assertEquals("*/5 * * * * *", tmpProperties.getCron()),
        () -> assertEquals(20, tmpCleanAfter)
    );
  }

  @Test
  void testFileBinaryProperties() {
    TekFileProperties fileProperties = coreProperties.getFile();
    assertNotNull(fileProperties);
    val binProperties = fileProperties.getBinary();
    val binDirectory = binProperties.getDirectory();
    assertAll(
        () -> assertNotNull(binProperties),
        () -> assertNotNull(binDirectory)
    );
    assertAll(
        () -> assertEquals(new File("uploadDir"), binDirectory),
        () -> assertEquals("*/10 * * * * *", binProperties.getCron())
    );
  }
}
