package com.tek.core.config;

import com.tek.core.TekCoreProperties;
import com.tek.core.TekModuleConfiguration;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.naming.ConfigurationException;
import java.text.MessageFormat;
import java.util.Arrays;

import static com.tek.core.TekCoreConstant.TEK_CORE_CONFIGURATION;
import static com.tek.core.TekProfile.*;
import static java.lang.String.join;

/**
 * Tek Core Module Configuration:
 * <p>
 * - Checks Spring active profile configuration, after environment has been injected
 *
 * @author MarcoPagan
 */
@Configuration(TEK_CORE_CONFIGURATION)
@Slf4j
public class TekCoreModuleConfiguration extends TekModuleConfiguration {

  @Autowired
  private TekCoreProperties coreProperties;

  private final String newLine = System.getProperty("line.separator");

  public TekCoreModuleConfiguration() {
    super(TekCoreModuleConfiguration.class);
  }

  @Override
  @SneakyThrows
  public void checkModuleConfiguration() {
    checkActiveProfile();
    checkConditionalProperties();
  }

  private void checkActiveProfile() throws ConfigurationException {
    val activeProfiles = Arrays.asList(environment.getActiveProfiles());
    boolean matchProfile =
        activeProfiles.contains(DEVELOPMENT) ||
            activeProfiles.contains(TEST) ||
            activeProfiles.contains(PRODUCTION);

    if (!matchProfile) {
      String errorMessage =
          join("", newLine)
              .concat("Spring active profile NOT FOUND! ")
              .concat("Evaluate the property [spring.profiles.active: <some-profile>] ")
              .concat("in your application.yaml/properties file.")
              .concat(newLine)
              .concat("If the property evaluates, ")
              .concat("check your classpath configuration or Maven pom.xml ")
              .concat("(if you are using maven resource filtering) ")
              .concat("and try to re-build your project ")
              .concat("or run Maven with the following goals: clean, compile.");
      throw new ConfigurationException(errorMessage);
    }

    log.info("Running with Spring profile(s): {}", activeProfiles.toString());

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

  private void checkConditionalProperties() throws ConfigurationException {
    checkMailErrorHandling();
  }

  private void checkMailErrorHandling() throws ConfigurationException {
    val sendErrors = coreProperties.getMail().isSendErrors();
    val isActiveScheduler = coreProperties.getScheduler().getActive();

    if (sendErrors && !isActiveScheduler) {
      val errorMessage =
          join("", newLine)
              .concat("Email error handling is active but scheduled cleanup of directories is not!")
              .concat(newLine)
              .concat("Set property [tek.core.scheduler.active: true]");
      throw new ConfigurationException(errorMessage);
    }
  }
}
