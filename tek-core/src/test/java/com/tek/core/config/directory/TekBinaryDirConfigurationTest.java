package com.tek.core.config.directory;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tek.core.properties.TekCoreProperties;
import com.tek.core.service.TekFileService;
import java.io.File;
import org.junit.jupiter.api.AfterAll;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = TekCoreProperties.class)
@ContextConfiguration(
    classes = {
        TekFileService.class,
        TekBinaryDirConfiguration.class
    }
)
@TestPropertySource(
    properties = {
        "tek.core.fileConfiguration.binary.enabled=true",
        "tek.core.fileConfiguration.binary.directoryPath=C:/Users/MarcoPagan/Desktop/binDir"
    })
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class TekBinaryDirConfigurationTest {

  @Autowired private TekCoreProperties properties;
  @Autowired private TekBinaryDirConfiguration configuration;

  @BeforeAll
  @Test
  void setup() {
    Assertions.assertAll(
        () -> assertNotNull(properties),
        () -> assertNotNull(configuration)
    );
  }

  @Test
  @Order(1)
  void test_BinaryDirectoryConfiguration() {
    final var propDir = properties.getFileConfiguration().getBinary().getDirectoryPath();
    final var createdDir = configuration.binaryDirectory();
    checkDirectoryConfiguration(propDir, createdDir);
  }

  @AfterAll
  void deleteCreatedDirectory() {
    properties.getFileConfiguration().getBinary().getDirectoryPath().deleteOnExit();
  }

  private void checkDirectoryConfiguration(File propDir, File createdDir) {
    Assertions.assertAll(
        () -> assertTrue(createdDir.isDirectory()),
        () -> assertTrue(createdDir.isAbsolute()),
        () -> assertEquals(propDir.getName(), createdDir.getName())
    );
  }
}
