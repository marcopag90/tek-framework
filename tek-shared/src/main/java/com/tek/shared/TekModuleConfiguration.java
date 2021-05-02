package com.tek.shared;

import javax.annotation.PostConstruct;
import javax.naming.ConfigurationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

/**
 * Template class to create a Tek Module configuration.
 *
 * @author MarcoPagan
 */
@RequiredArgsConstructor
@Slf4j
public abstract class TekModuleConfiguration {

  protected final ApplicationContext context;

  public abstract void checkModuleConfiguration() throws ConfigurationException;

  @SuppressWarnings("unused")
  @PostConstruct
  private void setup() throws ConfigurationException {
    log.info("Checking Tek Module Configuration: {}",
        ClassUtils.getUserClass(this).getSimpleName()
    );
    checkModuleConfiguration();
    log.info(
        "{} success!",
        ClassUtils.getUserClass(this).getSimpleName()
    );
  }
}