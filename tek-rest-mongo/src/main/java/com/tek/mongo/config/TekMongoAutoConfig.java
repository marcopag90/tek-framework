package com.tek.mongo.config;

import com.tek.mongo.TekMongoQueryController;
import com.tek.shared.TekModuleConfiguration;
import java.util.List;
import javax.naming.ConfigurationException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

@Configuration
@Slf4j
public class TekMongoAutoConfig extends TekModuleConfiguration {

  @Nullable
  @Autowired
  private List<TekMongoQueryController> controllers;

  public TekMongoAutoConfig(ApplicationContext context) {
    super(context);
  }

  @Override
  public void checkModuleConfiguration() throws ConfigurationException {
    val className = TekMongoQueryController.class.getSimpleName();
    if (CollectionUtils.isEmpty(controllers)) {
      log.warn("No implementation of {} was found.", className);
      log.warn("If you don't need a common collection querable controller, you can ignore this.");
    } else if (controllers.size() > 1) {
      val message = String.format("There must be only one instance of %s", className);
      throw new ConfigurationException(message);
    }
  }

}
