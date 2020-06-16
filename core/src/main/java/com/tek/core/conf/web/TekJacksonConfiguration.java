package com.tek.core.conf.web;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Jackson Module configuration:
 * <ul>
 *     <li>
 *      Provides a handler interceptor to avoid MappingJackson2HttpMessageConverter serialization
 *      failure on lazy proxy objects retrieved from Hibernate, when no session is in context.
 *     </li>
 *     <li>
 *      Provides {@link GuavaModule} to allow serialization/deserialization of Guava collections
 *     </li>
 * </ul>
 *
 * @author MarcoPagan
 */
@Configuration
public class TekJacksonConfiguration implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                ObjectMapper objectMapper =
                    ((MappingJackson2HttpMessageConverter) converter).getObjectMapper();
                objectMapper.registerModule(new Hibernate5Module());
            }
        }
    }

    @Bean
    public Module guavaModule() {
        return new GuavaModule();
    }
}
