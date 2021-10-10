package com.tek.core.config.directory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tek.core.properties.TekCoreProperties;
import java.io.File;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
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
        TekBinaryDirConfiguration.class,
        TekTmpDirConfiguration.class
    })
@TestPropertySource(
    properties = {
        "tek.core.fileConfiguration.tmp.enabled=true",
        "tek.core.fileConfiguration.tmp.directoryPath=C:/Users/MarcoPagan/Desktop/tmpDir",
        "tek.core.fileConfiguration.binary.enabled=true",
        "tek.core.fileConfiguration.binary.directoryPath=C:/Users/MarcoPagan/Desktop/binDir"
    })
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class TekDirConfigurationTest {

  @Autowired private TekCoreProperties properties;
  @Autowired private TekBinaryDirConfiguration binaryDirConfiguration;
  @Autowired private TekTmpDirConfiguration tmpDirConfiguration;

  @Test
  @Order(1)
  void test_tmpDirectoryConfiguration() {
    final var propDir = properties.getFileConfiguration().getTmp().getDirectoryPath();
    final var createdDir = tmpDirConfiguration.tmpDirectory();
    checkDirectoryConfiguration(propDir, createdDir);
  }

  @Test
  @Order(2)
  void test_BinaryDirectoryConfiguration() {
    final var propDir = properties.getFileConfiguration().getBinary().getDirectoryPath();
    final var createdDir = binaryDirConfiguration.binaryDirectory();
    checkDirectoryConfiguration(propDir, createdDir);
  }

  @AfterAll
  void deleteCreatedDirectories() {
    properties.getFileConfiguration().getBinary().getDirectoryPath().deleteOnExit();
    properties.getFileConfiguration().getTmp().getDirectoryPath().deleteOnExit();
  }

  private void checkDirectoryConfiguration(File propDir, File createdDir) {
    Assertions.assertAll(
        () -> assertTrue(createdDir.isDirectory()),
        () -> assertTrue(createdDir.isAbsolute()),
        () -> assertEquals(propDir.getName(), createdDir.getName())
    );
  }
}
