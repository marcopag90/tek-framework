package com.tek.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import static com.tek.core.TekCoreConstant.GIT_PROPERTIES;

/**
 * Configuration to allow access of different properties file through the application just by using
 * its {@link org.springframework.beans.factory.annotation.Value} injection.
 *
 * @author MarcoPagan
 */
@Configuration
public class TekPropertyConfiguration {

  @Bean
  public PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
    PropertySourcesPlaceholderConfigurer propsConfig = new PropertySourcesPlaceholderConfigurer();
    propsConfig.setLocations(new ClassPathResource(GIT_PROPERTIES));
    propsConfig.setIgnoreResourceNotFound(true);
    propsConfig.setIgnoreUnresolvablePlaceholders(true);
    return propsConfig;
  }
}
