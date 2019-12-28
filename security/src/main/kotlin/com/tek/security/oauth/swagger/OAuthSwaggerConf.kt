package com.tek.security.oauth.swagger

import com.google.common.collect.Lists
import com.tek.core.swagger.SwaggerIgnore.ignoredParameters
import com.tek.core.util.LoggerDelegate
import com.tek.security.oauth.configuration.OAuthWebSecurity
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 * Configuration for Swagger with OAuth2 Authentication
 */
@Configuration
@EnableSwagger2
@ConditionalOnBean(OAuthWebSecurity::class)
class OAuthSwaggerConf(
    private val securityScheme: SecurityScheme,
    private val securityContext: SecurityContext
) {

    private val log by LoggerDelegate()

    @Bean
    fun api(): Docket {

        log.info("Swagger security type: [oauth2]")

        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController::class.java))
            .paths(PathSelectors.any())
            .build()
            .securitySchemes(Lists.newArrayList(securityScheme))
            .securityContexts(Lists.newArrayList(securityContext))
            .ignoredParameterTypes(*ignoredParameters())
    }
}

