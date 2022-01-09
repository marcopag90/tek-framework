package com.tek.core.config.directory;

import static com.tek.core.constants.TekCoreConstants.TEK_CORE_PREFIX;

import com.tek.core.TekCoreAutoConfig;
import com.tek.core.properties.TekCoreProperties;
import com.tek.shared.io.TekFileUtils;
import java.io.File;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration to set up file directories used to store binaries files.
 *
 * @author MarcoPagan
 */
@Configuration
@ConditionalOnClass(TekCoreAutoConfig.class)
@ConditionalOnProperty(
    prefix = TEK_CORE_PREFIX,
    name = "fileConfiguration.binary.enabled",
    havingValue = "true"
)
@RequiredArgsConstructor
@Slf4j
public class TekBinaryDirConfiguration {

  private final TekCoreProperties coreProperties;

  private File binaryDirectoryPath;

  @PostConstruct
  private void init() {
    this.binaryDirectoryPath = coreProperties.getFileConfiguration().getBinary().getDirectoryPath();
  }

  /**
   * Directory where to store binary files.
   */
  @Bean
  public File binaryDirectory() {
    if (!this.binaryDirectoryPath.isDirectory()) {
      try {
        final var dir = TekFileUtils.createDirectory(binaryDirectoryPath.toPath().toString());
        log.info("Creating directory: [{}]", dir.getAbsolutePath());
        return dir;
      } catch (Exception e) {
        log.error("Could not create directory: {}", binaryDirectoryPath.getAbsolutePath());
      }
    }
    return binaryDirectoryPath;
  }
}
