package com.tek.mongo.config;

import com.tek.mongo.TekMongoAutoConfig;
import com.tek.mongo.controller.TekMongoCollectionController;
import com.tek.shared.TekModuleConfiguration;
import java.util.List;
import javax.naming.ConfigurationException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

@Configuration
@ConditionalOnClass(value = {TekMongoAutoConfig.class})
@Slf4j
public class TekMongoModuleConfiguration extends TekModuleConfiguration {

  @Nullable
  @Autowired
  private List<TekMongoCollectionController> controllers;

  public TekMongoModuleConfiguration(ApplicationContext context) {
    super(context);
  }

  @Override
  public void checkModuleConfiguration() throws ConfigurationException {
    final var className = TekMongoCollectionController.class.getSimpleName();
    if (CollectionUtils.isEmpty(controllers)) {
      log.warn("No implementation of {} was found.", className);
      log.warn("If you don't need a common collection querable controller, you can ignore this.");
    } else if (controllers.size() > 1) {
      final var message = String.format("There must be only one instance of %s", className);
      throw new ConfigurationException(message);
    }
  }

}
