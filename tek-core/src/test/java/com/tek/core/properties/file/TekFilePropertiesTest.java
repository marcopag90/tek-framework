package com.tek.core.properties.file;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.tek.core.properties.TekCoreProperties;
import java.io.File;
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
@TestPropertySource("classpath:file-custom.properties")
@TestMethodOrder(OrderAnnotation.class)
class TekFilePropertiesTest {

  @Autowired
  private TekCoreProperties coreCustomProperties;

  @Test
  @Order(1)
  void tmpDefaultValues() {
    final var fileProperties = new TekCoreProperties().getFileConfiguration();
    assertNotNull(fileProperties);
    final var tmpProperties = fileProperties.getTmp();
    File tmpDirectory = tmpProperties.getDirectory();
    Integer tmpCleanAfter = tmpProperties.getCleanAfter();
    assertAll(
        () -> assertEquals(new File("tmp"), tmpDirectory),
        () -> assertEquals("0 0 * * * *", tmpProperties.getCron()),
        () -> assertEquals(10, tmpCleanAfter)
    );
  }

  @Test
  @Order(2)
  void tmpCustomValues() {
    final var fileProperties = coreCustomProperties.getFileConfiguration();
    assertNotNull(fileProperties);
    final var tmpProperties = fileProperties.getTmp();
    File tmpDirectory = tmpProperties.getDirectory();
    Integer tmpCleanAfter = tmpProperties.getCleanAfter();
    assertAll(
        () -> assertEquals(new File("tmpDir"), tmpDirectory),
        () -> assertEquals("*/5 * * * * *", tmpProperties.getCron()),
        () -> assertEquals(20, tmpCleanAfter)
    );
  }

  @Test
  @Order(3)
  void binaryDefaultValues() {
    final var fileProperties = new TekCoreProperties().getFileConfiguration();
    assertNotNull(fileProperties);
    final var binProperties = fileProperties.getBinary();
    final var binDirectory = binProperties.getDirectory();
    assertAll(
        () -> assertNotNull(binProperties),
        () -> assertNotNull(binDirectory)
    );
    assertAll(
        () -> assertEquals(new File("upload"), binDirectory),
        () -> assertEquals("0 0 * * * *", binProperties.getCron())
    );
  }

  @Test
  @Order(4)
  void binaryCustomValues() {
    final var fileProperties = coreCustomProperties.getFileConfiguration();
    assertNotNull(fileProperties);
    final var binProperties = fileProperties.getBinary();
    final var binDirectory = binProperties.getDirectory();
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
