package com.tek.core.config.directory;

import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_TMP_DIR;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_TMP_DIR_CONFIGURATION;
import static com.tek.core.constants.TekCoreConstants.TEK_CORE_PREFIX;

import com.tek.core.TekCoreAutoConfig;
import com.tek.core.properties.TekCoreProperties;
import com.tek.shared.io.TekFileUtils;
import java.io.File;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration to set up file directories used by application.
 *
 * @author MarcoPagan
 */
@Configuration(TEK_CORE_TMP_DIR_CONFIGURATION)
@ConditionalOnClass(TekCoreAutoConfig.class)
@ConditionalOnProperty(
    prefix = TEK_CORE_PREFIX,
    name = "fileConfiguration.tmp.enabled", havingValue = "true"
)
public class TekTmpDirConfiguration {

  private final Logger log = LoggerFactory.getLogger(TekTmpDirConfiguration.class);

  @Autowired
  private TekCoreProperties coreProperties;

  private File tmpDirectory;

  @PostConstruct
  private void init() {
    this.tmpDirectory = coreProperties.getFileConfiguration().getTmp().getDirectoryPath();
  }

  /**
   * Directory where to store temporary application files.
   */
  @Bean(TEK_CORE_TMP_DIR)
  public File tmpDirectory() {
    if (!this.tmpDirectory.isDirectory()) {
      try {
        final var dir = TekFileUtils.createDirectory(tmpDirectory.toPath().toString());
        log.info("Creating directory: [{}]", dir.getAbsolutePath());
        return dir;
      } catch (Exception e) {
        log.error("Could not create directory: {}", tmpDirectory.getAbsolutePath());
      }
    }
    return tmpDirectory;
  }
}
