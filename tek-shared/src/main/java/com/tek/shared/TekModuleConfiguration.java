package com.tek.shared;

import javax.annotation.PostConstruct;
import javax.naming.ConfigurationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

/**
 * Template class to create a Tek Module configuration.
 *
 * @author MarcoPagan
 */
@RequiredArgsConstructor
@Slf4j
public abstract class TekModuleConfiguration {

  private final Class<?> configuration;

  public abstract void checkModuleConfiguration() throws ConfigurationException;

  @SuppressWarnings("unused")
  @PostConstruct
  private void setup() throws ConfigurationException {
    log.info(
        "Checking Tek Module Configuration: {}",
        ClassUtils.getUserClass(configuration).getSimpleName()
    );
    checkModuleConfiguration();
    log.info(
        "{} success!",
        ClassUtils.getUserClass(configuration).getSimpleName()
    );
  }
}
