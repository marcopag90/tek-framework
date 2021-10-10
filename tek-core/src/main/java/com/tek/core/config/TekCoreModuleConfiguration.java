package com.tek.core.config;

import static com.tek.core.constants.TekCoreBeanConstants.TEK_CORE_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanConstants.TEK_CORE_PROP_PLACEHOLDER_CONF_BEAN;
import static com.tek.core.constants.TekCoreConstants.GIT_PROPERTIES;
import static java.lang.String.join;

import com.tek.core.TekCoreAutoConfig;
import com.tek.core.properties.TekCoreProperties;
import com.tek.shared.TekModuleConfiguration;
import javax.naming.ConfigurationException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/**
 * Tek Core Module Configuration:
 * <p>
 * - Checks Spring active profile configuration after environment has been injected
 *
 * @author MarcoPagan
 */
@Configuration(TEK_CORE_CONFIGURATION)
@ConditionalOnClass(TekCoreAutoConfig.class)
@EnableConfigurationProperties(TekCoreProperties.class)
@Slf4j
public class TekCoreModuleConfiguration extends TekModuleConfiguration {

  private final String newLine = System.getProperty("line.separator");
  private final TekCoreProperties coreProperties;

  public TekCoreModuleConfiguration(
      ApplicationContext context,
      TekCoreProperties coreProperties
  ) {
    super(context);
    this.coreProperties = coreProperties;
  }

  @Bean(TEK_CORE_PROP_PLACEHOLDER_CONF_BEAN)
  public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
    final var propsConfig = new PropertySourcesPlaceholderConfigurer();
    propsConfig.setLocations(new ClassPathResource(GIT_PROPERTIES));
    propsConfig.setIgnoreResourceNotFound(true);
    propsConfig.setIgnoreUnresolvablePlaceholders(true);
    return propsConfig;
  }

  @Override
  public void checkModuleConfiguration() throws ConfigurationException {
    checkConditionalProperties();
  }

  private void checkConditionalProperties() {
    checkMailErrorHandling();
  }

  @SneakyThrows
  private void checkMailErrorHandling() {
    final var sendErrors = coreProperties.getMailConfiguration().isSendErrors();
    final var isActiveScheduler =
        coreProperties.getFileConfiguration().getTmp().isSchedulerEnabled();
    if (sendErrors && !isActiveScheduler) {
      final var errorMessage =
          join("", newLine)
              .concat("Email error handling is active but scheduled cleanup of directories is not!")
              .concat(newLine)
              .concat("Set property tek.core.scheduler.active: true");
      throw new ConfigurationException(errorMessage);
    }
  }
}
