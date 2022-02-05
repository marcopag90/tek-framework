package com.tek.core.config;

import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_MODULE_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_PROP_PLACEHOLDER_CONFIGURATION;
import static com.tek.core.constants.TekCoreConstants.GIT_PROPERTIES;
import static java.lang.String.join;

import com.tek.core.TekCoreAutoConfig;
import com.tek.core.properties.TekCoreProperties;
import com.tek.shared.TekModuleConfiguration;
import com.tek.shared.exception.TekModuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/**
 * Tek Core Module Configuration
 *
 * @author MarcoPagan
 */
@Configuration(TEK_CORE_MODULE_CONFIGURATION)
@ConditionalOnClass(TekCoreAutoConfig.class)
@EnableConfigurationProperties(TekCoreProperties.class)
public class TekCoreModuleConfiguration extends TekModuleConfiguration {

  private final Logger log = LoggerFactory.getLogger(TekCoreModuleConfiguration.class);
  private final String newLine = System.getProperty("line.separator");
  private final TekCoreProperties coreProperties;

  public TekCoreModuleConfiguration(
      ApplicationContext context,
      TekCoreProperties coreProperties
  ) {
    super(context);
    this.coreProperties = coreProperties;
  }

  @Bean(TEK_CORE_PROP_PLACEHOLDER_CONFIGURATION)
  public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
    final var propsConfig = new PropertySourcesPlaceholderConfigurer();
    propsConfig.setLocations(new ClassPathResource(GIT_PROPERTIES));
    propsConfig.setIgnoreResourceNotFound(true);
    propsConfig.setIgnoreUnresolvablePlaceholders(true);
    return propsConfig;
  }

  @Override
  public void checkModuleConfiguration() throws TekModuleException {
    final var sendErrors = coreProperties.getMailConfiguration().isSendErrors();
    final var isActiveScheduler =
        coreProperties.getFileConfiguration().getTmp().isSchedulerEnabled();
    if (sendErrors && !isActiveScheduler) {
      final var errorMessage =
          join("", newLine)
              .concat("Email error handling is active but scheduled cleanup of directories is not!")
              .concat(newLine)
              .concat("Set property tek.core.scheduler.active: true");
      throw new TekModuleException(errorMessage);
    }
  }
}
