package com.tek.rest.shared.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.tek.rest.shared.TekRestSharedAutoConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
@ConditionalOnClass(TekRestSharedAutoConfig.class)
public class TekJacksonConfig {

  /**
   * Enables wrapping for {@link com.fasterxml.jackson.annotation.JsonRootName}
   */
  @Bean
  public Jackson2ObjectMapperBuilder jacksonBuilder() {
    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
    builder.featuresToEnable(SerializationFeature.WRAP_ROOT_VALUE);
    return builder;
  }
}
