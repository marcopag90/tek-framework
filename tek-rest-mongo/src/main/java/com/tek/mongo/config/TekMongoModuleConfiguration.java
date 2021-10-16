package com.tek.mongo.config;

import com.tek.mongo.TekMongoAutoConfig;
import com.tek.shared.TekModuleConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(value = {TekMongoAutoConfig.class})
@Slf4j
public class TekMongoModuleConfiguration extends TekModuleConfiguration {


  public TekMongoModuleConfiguration(ApplicationContext context) {
    super(context);
  }

  @Override
  public void checkModuleConfiguration() {
    //noop
  }

}
