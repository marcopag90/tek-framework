package com.tek.core.config.directory;

import static com.tek.core.constants.TekCoreConstants.TEK_CORE_PREFIX;

import com.tek.core.TekCoreAutoConfig;
import com.tek.core.properties.TekCoreProperties;
import com.tek.core.service.TekFileService;
import java.io.File;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

  private final TekCoreProperties coreProperties;
  private final TekFileService fileService;

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
        final var dir = fileService.createDirectory(tmpDirectory.toPath().toString());
        log.info("Creating directory: [{}]", dir.getAbsolutePath());
        return dir;
      } catch (Exception e) {
        log.error("Could not create directory: {}", tmpDirectory.getAbsolutePath());
      }
    }
    return tmpDirectory;
  }
}
