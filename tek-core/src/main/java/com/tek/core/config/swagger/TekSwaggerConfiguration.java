package com.tek.core.config.swagger;

import com.tek.core.TekCoreAutoConfig;
import com.tek.core.properties.TekCoreProperties;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger Configuration
 *
 * @author MarcoPagan
 */
@Configuration
@ConditionalOnClass(TekCoreAutoConfig.class)
@EnableSwagger2
@RequiredArgsConstructor
public class TekSwaggerConfiguration {

  @NonNull private final ApplicationContext context;
  @NonNull private final TekCoreProperties coreProperties;

  private final Class<?>[] defaultIgnoredParameterTypes = {Pageable.class, Page.class, Sort.class};

  @Value("${git.build.version:version not available}")
  private String gitBuildVersion;

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(getApiInfo())
        .ignoredParameterTypes(defaultIgnoredParameterTypes)
        .ignoredParameterTypes(getIgnoredParameterTypes());
  }

  @Bean
  public ApiInfo getApiInfo() {
    final var swaggerProperties = coreProperties.getSwagger();
    return new ApiInfo(
        context.getApplicationName(),
        swaggerProperties.getDescription(),
        gitBuildVersion,
        swaggerProperties.getTermOfServiceUrl(),
        new Contact(
            swaggerProperties.getContactName(),
            swaggerProperties.getContactUrl(),
            swaggerProperties.getContactEmail()
        ),
        swaggerProperties.getLicense(),
        swaggerProperties.getLicenseUrl(),
        Collections.emptyList()
    );
  }

  private Class<?>[] getIgnoredParameterTypes() {
    final var swaggerIgnores = context.getBeansOfType(SwaggerIgnore.class).values();
    return swaggerIgnores.stream().map(SwaggerIgnore::ignore).collect(Collectors.toSet())
        .stream().flatMap(Arrays::stream).toArray(Class[]::new);
  }
}
