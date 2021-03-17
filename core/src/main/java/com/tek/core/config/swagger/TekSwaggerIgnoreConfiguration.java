package com.tek.core.config.swagger;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration to tell Swagger what classed must be ignored by <i>Swagger Models</i>.
 *
 * @author MarcoPagan
 */

@Configuration
@RequiredArgsConstructor
public class TekSwaggerIgnoreConfiguration {

  @NonNull private final List<SwaggerIgnore> swaggerIgnores;

  public Class<?>[] getIgnoredParameterTypes() {
    return swaggerIgnores.stream().map(SwaggerIgnore::ignore).collect(Collectors.toSet())
        .stream().flatMap(Arrays::stream).toArray(Class[]::new);
  }
}
