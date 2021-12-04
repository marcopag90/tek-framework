package com.tek.shared;

import com.tek.shared.exception.TekModuleException;
import javax.annotation.PostConstruct;
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
