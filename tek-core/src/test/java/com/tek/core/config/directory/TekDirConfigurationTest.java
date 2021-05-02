package com.tek.core.config.directory;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = TekCoreProperties.class)
@ContextConfiguration(classes = TekDirConfiguration.class)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class TekDirConfigurationTest {

  @Autowired private TekDirConfiguration configuration;
  @Autowired private TekCoreProperties properties;

  @Test
  @Order(1)
  void testTmpDirectory() {
    final var propDir = properties.getFile().getTmp().getDirectory();
    final var createdDir = configuration.tmpDirectory();
    internalTestDir(propDir, createdDir);
  }

  @Test
  @Order(2)
  void testBinaryDirectory() {
    final var propDir = properties.getFile().getBinary().getDirectory();
    final var createdDir = configuration.binaryDirectory();
    internalTestDir(propDir, createdDir);
  }

  @AfterAll
  void deleteCreatedDirectories() {
    properties.getFile().getBinary().getDirectory().deleteOnExit();
    properties.getFile().getTmp().getDirectory().deleteOnExit();
  }

  private void internalTestDir(File propDir, File createdDir) {
    Assertions.assertTrue(createdDir.isDirectory());
    Assertions.assertEquals(propDir.getName(), createdDir.getName());
  }

}
