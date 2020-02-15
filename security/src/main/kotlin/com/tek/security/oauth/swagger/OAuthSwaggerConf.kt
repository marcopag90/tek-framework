package com.tek.security.oauth.swagger

import com.google.common.base.Predicates
import com.google.common.collect.Lists
import com.tek.core.swagger.SwaggerApiInfo
import com.tek.core.swagger.SwaggerIgnore.ignoredParameters
import com.tek.security.SecurityPattern.JAVERS_PATH
import com.tek.security.SecurityPattern.PRIVILEGE_PATH
import com.tek.security.SecurityPattern.ROLE_PATH
import com.tek.security.SecurityPattern.USER_PATH
import com.tek.security.SecurityPattern.WEBAUDIT_PATH
import com.tek.security.oauth.configuration.OAuthWebSecurity
import com.tek.security.util.antPath
import org.slf4j.LoggerFactory
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
@Suppress("UNUSED")
@Configuration
@EnableSwagger2
@ConditionalOnBean(OAuthWebSecurity::class)
class OAuthSwaggerConf(
    private val securityScheme: SecurityScheme,
    private val securityContext: SecurityContext,
    private val swaggerApiInfo: SwaggerApiInfo
) {

    private val log = LoggerFactory.getLogger(OAuthSwaggerConf::class.java)

    @Bean
    fun adminApi(): Docket {

        log.info("Swagger security type: [oauth2] with adminApi()")

        return Docket(DocumentationType.SWAGGER_2)
            .groupName("Administration")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController::class.java))
            .paths(
                Predicates.or(
                    PathSelectors.ant(ROLE_PATH.antPath()),
                    PathSelectors.ant(PRIVILEGE_PATH.antPath()),
                    PathSelectors.ant(USER_PATH.antPath())
                )
            )
            .build()
            .apiInfo(swaggerApiInfo.tekApiInfo())
            .securitySchemes(Lists.newArrayList(securityScheme))
            .securityContexts(Lists.newArrayList(securityContext))
            .ignoredParameterTypes(*ignoredParameters())
    }

    @Bean
    fun auditApi(): Docket {

        log.info("Swagger security type: [oauth2] with auditApi()")

        return Docket(DocumentationType.SWAGGER_2)
            .groupName("Auditing")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController::class.java))
            .paths(
                Predicates.or(
                    PathSelectors.ant(JAVERS_PATH.antPath()),
                    PathSelectors.ant(WEBAUDIT_PATH.antPath())
                )
            )
            .build()
            .apiInfo(swaggerApiInfo.tekApiInfo())
            .securitySchemes(Lists.newArrayList(securityScheme))
            .securityContexts(Lists.newArrayList(securityContext))
            .ignoredParameterTypes(*ignoredParameters())
    }

    @Bean
    fun businessApi(): Docket {

        log.info("Swagger security type: [oauth2] with businessApi()")

        return Docket(DocumentationType.SWAGGER_2)
            .groupName("Business")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController::class.java))
            .paths(
                Predicates.and(
                    Predicates.not(PathSelectors.ant(ROLE_PATH.antPath())),
                    Predicates.not(PathSelectors.ant(PRIVILEGE_PATH.antPath())),
                    Predicates.not(PathSelectors.ant(USER_PATH.antPath())),
                    Predicates.not(PathSelectors.ant(JAVERS_PATH.antPath())),
                    Predicates.not(PathSelectors.ant(WEBAUDIT_PATH.antPath()))
                )
            )
            .build()
            .apiInfo(swaggerApiInfo.tekApiInfo())
            .securitySchemes(Lists.newArrayList(securityScheme))
            .securityContexts(Lists.newArrayList(securityContext))
            .ignoredParameterTypes(*ignoredParameters())
    }
}

