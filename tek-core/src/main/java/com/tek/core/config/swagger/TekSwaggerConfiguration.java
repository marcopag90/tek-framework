package com.tek.core.config.swagger;

import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_SWAGGER_API;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_SWAGGER_API_INFO;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_SWAGGER_CONFIGURATION;

import com.tek.core.TekCoreAutoConfig;
import com.tek.core.properties.TekCoreProperties;
import com.tek.rest.shared.swagger.SwaggerIgnore;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * Swagger Configuration
 *
 * @author MarcoPagan
 */
//TODO fix this: https://www.vincenzoracca.com/blog/framework/spring/openapi/
@Configuration(TEK_CORE_SWAGGER_CONFIGURATION)
@ConditionalOnClass(TekCoreAutoConfig.class)
//@EnableSwagger2
public class TekSwaggerConfiguration {

  @Autowired private ApplicationContext context;
  @Autowired private TekCoreProperties coreProperties;

  private final Class<?>[] defaultIgnoredParameterTypes = {Pageable.class, Page.class, Sort.class};

  @Value("${git.build.version:undefined}")
  private String gitBuildVersion;

  @Bean(TEK_CORE_SWAGGER_API)
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo())
        .ignoredParameterTypes(defaultIgnoredParameterTypes)
        .ignoredParameterTypes(getIgnoredParameterTypes());
  }

  @Bean(TEK_CORE_SWAGGER_API_INFO)
  public ApiInfo apiInfo() {
    final var swaggerProperties = coreProperties.getSwaggerConfiguration();
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
