package com.tek.core.file;

import com.tek.core.TekCoreProperties;
import com.tek.core.conf.TekDirConfiguration;
import com.tek.core.service.TekFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@SpringBootTest
@TestPropertySource(locations = "classpath:core-file.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class TekTmpFileTest {

  @Autowired
  @SuppressWarnings("unused")
  private TekCoreProperties coreProperties;

  @Autowired
  @SuppressWarnings("unused")
  private TekFileService fileService;

  private File directory;
  private Integer cleanAfter;

  @PostConstruct
  private void init() {
    directory = coreProperties.getFile().getTmp().getDirectory();
    cleanAfter = coreProperties.getFile().getTmp().getCleanAfter();
  }

  @Test
  @Order(1)
  public void testCreateTmpFiles() {
    if (!directory.exists()) {
      log.error(
          "Directory {} doesn't exist. Check [{}]",
          TekDirConfiguration.class.getSimpleName(),
          directory
      );
      throw new TestAbortedException("Aborted test due to missing directory");
    }
    LocalDate today = LocalDate.now();
    log.info("Today date: {}", today);
    LocalDate start = today.minusDays(cleanAfter);
    log.info("Creating test directories starting from date {}", start);
    while (start.isBefore(today)) {
      String localDir = directory + File.separator + start;
      for (int i = 0; i < cleanAfter; i++) {
        String fileName = "test_" + i + ".txt";
        fileService.deepCreate(localDir, fileName);
      }
      start = start.plusDays(1);
    }
    int expectedNumberOfFiles = cleanAfter * cleanAfter;
    int createdNumberOfFiles = FileUtils.listFiles
        (directory, new String[]{"txt"}, true).size();

    Assertions.assertEquals(expectedNumberOfFiles, createdNumberOfFiles);
  }

  @Test
  @Order(2)
  public void testCleanTmpDirectory() throws IOException {
    int expectedNumberOfDeleteFiles = cleanAfter * cleanAfter;
    int deletedNumberOfFiles = FileUtils.listFiles
        (directory, new String[]{"txt"}, true).size();

    if (deletedNumberOfFiles == 0) {
      log.error("Directory {} is empty", directory);
      throw new TestAbortedException("Aborted test due to empty directory");
    }

    LocalDate today = LocalDate.now();
    log.info("Today date: {}", today);
    LocalDate start = today.minusDays(cleanAfter);
    log.info("cleanAfter parameter: {} days, starting date is {}", cleanAfter, start);

    while (start.isBefore(today)) {
      File dir = Paths.get(directory + File.separator + start).toFile();
      if (!directory.exists()) {
        log.error(
            "Directory {} doesn't exist. Check [{}]",
            TekDirConfiguration.class.getSimpleName(),
            directory
        );
        throw new TestAbortedException("Aborted test due to missing directory");
      }
      log.info("Attempting to delete directory {}...", dir.getAbsolutePath());
      fileService.deepDelete(dir);
      start = start.plusDays(1);
    }
    fileService.deepDelete(directory.getAbsoluteFile());

    Assertions.assertEquals(expectedNumberOfDeleteFiles, deletedNumberOfFiles);
    Assertions.assertFalse(directory.exists());
  }
}
