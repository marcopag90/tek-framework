package com.tek.shared;

import com.tek.shared.exception.TekModuleException;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

/**
 * Template class to create a Tek Module configuration.
 *
 * @author MarcoPagan
 */
public abstract class TekModuleConfiguration {

  protected final Logger log = LoggerFactory.getLogger(TekModuleConfiguration.class);
  protected final ApplicationContext context;

  protected TekModuleConfiguration(ApplicationContext context) {
    this.context = context;
  }

  public abstract void checkModuleConfiguration() throws TekModuleException;

  @SuppressWarnings("unused")
  @PostConstruct
  private void setup() throws TekModuleException {
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
