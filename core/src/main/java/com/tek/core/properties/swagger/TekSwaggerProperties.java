package com.tek.core.properties.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static com.tek.core.TekCoreConstant.TEK_SWAGGER_API_INFO;

/**
 * Configuration properties for Swagger.
 * <p>
 * Fallback to default configuration if none provided.
 *
 * @author MarcoPagan
 */
@Configuration
@ConfigurationProperties(prefix = TEK_SWAGGER_API_INFO)
@Data
public class TekSwaggerProperties {

    private String description = "Tek Framework API Service";
    private String termOfServiceUrl = null;

    private String contactName = null;
    private String contactUrl = null;
    private String contactEmail = null;

    private String license = null;
    private String licenseUrl = null;
}
