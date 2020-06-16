package com.tek.core.conf.swagger;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Configuration to tell Swagger what classed must be ignored by <i>Swagger Models</i>.
 * <p>
 * Accessing the {@link ApplicationContext}, collect all beans implementing {@link SwaggerIgnore}
 * to create a flat map of ignorable class types.
 *
 * @author MarcoPagan
 */

@Configuration
@RequiredArgsConstructor
public class TekSwaggerIgnoreConfiguration {

    @NonNull private final ApplicationContext context;

    @Bean
    public Class<?>[] getIgnoredParameterTypes() {
        Collection<SwaggerIgnore> beans = context.getBeansOfType(SwaggerIgnore.class).values();
        return beans.stream().map(SwaggerIgnore::ignore).collect(Collectors.toList())
            .stream().flatMap(List::stream).toArray(Class[]::new);
    }
}
