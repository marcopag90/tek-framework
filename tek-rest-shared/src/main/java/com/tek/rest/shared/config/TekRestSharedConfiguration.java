package com.tek.rest.shared.config;

import static com.tek.rest.shared.constants.TekRestSharedBeanNames.TEK_REST_SHARED_CONFIGURATION;
import static com.tek.rest.shared.constants.TekRestSharedBeanNames.TEK_REST_SHARED_GUAVA_MODULE;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.tek.rest.shared.TekRestSharedAutoConfig;
import com.tek.shared.TekModuleConfiguration;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <ul>
 *   <li>
 *     Provides a json deserializer to trim all incoming json parameters from web requests;
 *   </li>
 *   <li>
 *     Provides a {@link GuavaModule} to allow serialization/deserialization of Guava collections;
 *   </li>
 *   <li>
 *     Provides a customization of the default Spring Boot {@link com.fasterxml.jackson.databind.ObjectMapper}.
 *   </li>
 * </ul>
 *
 * @author MarcoPagan
 */
@Configuration(TEK_REST_SHARED_CONFIGURATION)
@ConditionalOnClass(TekRestSharedAutoConfig.class)
public class TekRestSharedConfiguration extends TekModuleConfiguration implements WebMvcConfigurer {

  public TekRestSharedConfiguration(ApplicationContext context) {
    super(context);
  }

  @Override
  public void checkModuleConfiguration() {
    //noop
  }

  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    for (HttpMessageConverter<?> converter : converters) {
      if (converter instanceof MappingJackson2HttpMessageConverter messageConverter) {
        final var mapper = messageConverter.getObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final var serializerModule = new SimpleModule();
        serializerModule.addDeserializer(String.class, new TrimSerializer(String.class));
        mapper.registerModules(serializerModule);
      }
    }
  }

  @Bean(TEK_REST_SHARED_GUAVA_MODULE)
  public Module guavaModule() {
    return new GuavaModule();
  }
}
