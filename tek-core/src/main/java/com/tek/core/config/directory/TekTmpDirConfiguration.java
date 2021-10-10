package com.tek.core.config.directory;

import static com.tek.core.constants.TekCoreConstants.TEK_CORE_PREFIX;

import com.tek.core.TekCoreAutoConfig;
import com.tek.core.properties.TekCoreProperties;
import java.io.File;
import java.nio.file.Files;
import javax.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//TODO tests

/**
 * Configuration to setup file directories used by application.
 *
 * @author MarcoPagan
 */
@Configuration
@ConditionalOnClass(TekCoreAutoConfig.class)
@ConditionalOnProperty(
    prefix = TEK_CORE_PREFIX,
    name = "fileConfiguration.tmp.enabled", havingValue = "true"
)
@RequiredArgsConstructor
@Slf4j
public class TekTmpDirConfiguration {

  @NonNull
  private final TekCoreProperties coreProperties;
  private File tmpDirectory;

  @PostConstruct
  private void init() {
    this.tmpDirectory = coreProperties.getFileConfiguration().getTmp().getDirectoryPath();
  }

  /**
   * Directory where to store temporary application files.
   */
  @Bean
  public File tmpDirectory() {
    if (!this.tmpDirectory.isDirectory()) {
      try {
        final var dir = Files.createDirectory(tmpDirectory.toPath()).toFile();
        log.info("Creating directory: [{}]", dir.getAbsolutePath());
        return dir;
      } catch (Exception e) {
        log.error("Could not create directory: {}", tmpDirectory.getAbsolutePath());
      }
    }
    return tmpDirectory;
  }
}
