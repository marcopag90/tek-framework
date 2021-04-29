package com.tek.core.config.web;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import java.util.List;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Jackson Module configuration:
 * <ul>
 *     <li>
 *      Provides a json deserializer to trim all incoming json parameters from web requests;
 *     </li>
 *     <li>
 *      Provides {@link GuavaModule} to allow serialization/deserialization of Guava collections.
 *     </li>
 * </ul>
 *
 * @author MarcoPagan
 */
@Configuration
public class TekCoreWebMvc implements WebMvcConfigurer {

  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    for (HttpMessageConverter<?> converter : converters) {
      if (converter instanceof MappingJackson2HttpMessageConverter) {
        val mapper = ((MappingJackson2HttpMessageConverter) converter).getObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        val serializerModule = new SimpleModule();
        serializerModule.addDeserializer(
            String.class,
            new StdDeserializer<String>(String.class) {
              @Override
              @SneakyThrows
              public String deserialize(JsonParser p, DeserializationContext ctx) {
                return p.getText() != null ? p.getText().trim() : null;
              }
            }
        );
        mapper.registerModules(serializerModule);
      }
    }
  }

  @Bean
  public Module guavaModule() {
    return new GuavaModule();
  }
}
