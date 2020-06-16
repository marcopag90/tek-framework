package com.tek.core.conf.swagger;

import com.tek.core.prop.swagger.TekSwaggerProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;

import java.util.Collections;

/**
 * Swagger ApiInfo Configuration.
 *
 * @author MarcoPagan
 */
@Configuration
@RequiredArgsConstructor
public class TekSwaggerInfoConfiguration {

    @NonNull private final TekSwaggerProperties swaggerProperties;
    @NonNull private final ApplicationContext context;

    @Value("${git.build.version}")
    private String gitBuildVersion;

    @Bean
    public ApiInfo getApiInfo() {
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
}
