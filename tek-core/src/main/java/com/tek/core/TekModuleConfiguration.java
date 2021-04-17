package com.tek.core;

import javax.annotation.PostConstruct;
import javax.naming.ConfigurationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

/**
 * Template class to create a Tek module configuration.
 *
 * @author MarcoPagan
 */
@RequiredArgsConstructor
@Slf4j
public abstract class TekModuleConfiguration {

  private final Class<?> configuration;

  public abstract void checkModuleConfiguration() throws ConfigurationException;

  @PostConstruct
  @SneakyThrows
  private void setup() {
    log.info(
        "Checking Tek Module Configuration: [{}]",
        ClassUtils.getUserClass(configuration).getSimpleName()
    );
    checkModuleConfiguration();
    log.info(
        "[{}] success!",
        ClassUtils.getUserClass(configuration).getSimpleName()
    );
  }
}
