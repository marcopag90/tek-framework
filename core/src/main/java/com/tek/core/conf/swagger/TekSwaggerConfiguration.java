package com.tek.core.conf.swagger;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger Configuration
 *
 * @author MarcoPagan
 */
@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class TekSwaggerConfiguration {

    @NonNull private final TekSwaggerInfoConfiguration infoConfiguration;
    @NonNull private final TekSwaggerIgnoreConfiguration ignoreConfiguration;

    private final Class<?>[] defaultIgnoredParameterTypes = {Pageable.class, Page.class, Sort.class};

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(infoConfiguration.getApiInfo())
            .ignoredParameterTypes(defaultIgnoredParameterTypes)
            .ignoredParameterTypes(ignoreConfiguration.getIgnoredParameterTypes());
    }
}
