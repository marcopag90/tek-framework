package com.tek.core.config;

import static com.tek.core.TekProfile.DEVELOPMENT;
import static com.tek.core.TekProfile.PRODUCTION;
import static com.tek.core.TekProfile.TEST;
import static com.tek.core.constants.TekCoreBeanConstants.TEK_CORE_CONFIGURATION;
import static java.lang.String.join;

import com.tek.core.TekCoreAutoConfig;
import com.tek.core.properties.TekCoreProperties;
import com.tek.shared.TekModuleConfiguration;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import javax.naming.ConfigurationException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

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

  @Override
  public void checkModuleConfiguration() throws ConfigurationException {
    checkActiveProfile();
    checkConditionalProperties();
  }

  private void checkActiveProfile() throws ConfigurationException {
    final var activeProfiles = Arrays.asList(context.getEnvironment().getActiveProfiles());
    final var toMatchProfiles = new ArrayList<String>();
    toMatchProfiles.add(DEVELOPMENT);
    toMatchProfiles.add(TEST);
    toMatchProfiles.add(PRODUCTION);
    final var matches = CollectionUtils.containsAny(activeProfiles, toMatchProfiles);
    if (!matches) {
      String warnMessage =
          join("", newLine)
              .concat(
                  String.format(
                      "Neither %s, %s or %s spring profile was found.",
                      DEVELOPMENT, PRODUCTION, TEST
                  )
              )
              .concat(newLine)
              .concat("Evaluate the property spring.profiles.active ")
              .concat("in your application.yaml/properties file.")
              .concat(newLine)
              .concat("If the property evaluates, ")
              .concat("check your classpath configuration and rebuild your project. ")
              .concat("If you are using Maven resource filtering via spring-boot-maven-plugin, ")
              .concat("try the following steps:")
              .concat(newLine)
              .concat("1) perform a Maven update")
              .concat(newLine)
              .concat("2) run Maven with the following goals: clean, compile")
              .concat(newLine)
              .concat("3) reboot your application.");
      log.warn(warnMessage);
    }
    log.info("Running with Spring profile(s): {}", activeProfiles);
    if (activeProfiles.contains(DEVELOPMENT) && activeProfiles.contains(PRODUCTION)) {
      String errorMessage =
          join("", newLine)
              .concat("Bad Spring active profile configuration! ")
              .concat(
                  MessageFormat.format("It should not run with both {0} ", DEVELOPMENT)
              )
              .concat(
                  MessageFormat.format("and {0} profiles at the same time!", PRODUCTION)
              );
      throw new ConfigurationException(errorMessage);
    }
  }

  private void checkConditionalProperties() {
    checkMailErrorHandling();
  }

  @SneakyThrows
  private void checkMailErrorHandling() {
    final var sendErrors = coreProperties.getMail().isSendErrors();
    final boolean isActiveScheduler = coreProperties.getScheduler().getActive();
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
