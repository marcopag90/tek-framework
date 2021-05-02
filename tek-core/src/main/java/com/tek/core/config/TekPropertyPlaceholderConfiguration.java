package com.tek.core.config;

import static com.tek.core.constants.TekCoreBeanConstants.TEK_CORE_PROP_PLACEHOLDER_CONF_BEAN;
import static com.tek.core.constants.TekCoreConstants.GIT_PROPERTIES;

import com.tek.core.TekCoreAutoConfig;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/**
 * Configuration to allow access of different properties file through the application just by using
 * its {@link org.springframework.beans.factory.annotation.Value} injection.
 *
 * @author MarcoPagan
 */
@Configuration
@ConditionalOnClass(TekCoreAutoConfig.class)
public class TekPropertyPlaceholderConfiguration {

  @Bean(TEK_CORE_PROP_PLACEHOLDER_CONF_BEAN)
  public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
    val propsConfig = new PropertySourcesPlaceholderConfigurer();
    propsConfig.setLocations(new ClassPathResource(GIT_PROPERTIES));
    propsConfig.setIgnoreResourceNotFound(true);
    propsConfig.setIgnoreUnresolvablePlaceholders(true);
    return propsConfig;
  }
}
