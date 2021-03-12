package com.tek.core.config;

import com.tek.core.TekCoreProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;

/**
 * Configuration to setup file directories used by application.
 *
 * @author MarcoPagan
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class TekDirConfiguration {

  @NonNull
  private final TekCoreProperties coreProperties;
  private File tmpDirectory;
  private File binaryDirectory;

  @PostConstruct
  private void init() {
    this.tmpDirectory = coreProperties.getFile().getTmp().getDirectory();
    this.binaryDirectory = coreProperties.getFile().getBinary().getDirectory();
  }

  /**
   * Directory where to store temporary application files.
   */
  @Bean
  public File tmpDirectory() {
    if (!this.tmpDirectory.isDirectory()) {
      try {
        File dir = Files.createDirectory(tmpDirectory.toPath()).toFile();
        log.info("Creating directory: [{}]", dir.getAbsolutePath());
        return dir;
      } catch (Exception e) {
        log.error("Could not create directory: {}", tmpDirectory.getAbsolutePath());
      }
    }
    return tmpDirectory;
  }

  /**
   * Directory where to store uploaded files.
   */
  @Bean
  public File binaryDirectory() {
    if (!this.binaryDirectory.isDirectory()) {
      try {
        File dir = Files.createDirectory(binaryDirectory.toPath()).toFile();
        log.info("Creating directory: [{}]", dir.getAbsolutePath());
        return dir;
      } catch (Exception e) {
        log.error("Could not create directory: {}", binaryDirectory.getAbsolutePath());
      }
    }
    return binaryDirectory;
  }
}
