package com.tek.security.swagger

import com.google.common.base.Predicates
import com.tek.core.swagger.SwaggerApiInfo
import com.tek.core.swagger.SwaggerIgnore.ignoredParameters
import com.tek.core.util.LoggerDelegate
import com.tek.security.SecurityPattern
import com.tek.security.SecurityPattern.PRIVILEGE_PATH
import com.tek.security.SecurityPattern.ROLE_PATH
import com.tek.security.SecurityPattern.USER_PATH
import com.tek.security.configuration.BasicWebSecurity
import com.tek.security.util.antPath
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 * Configuration for Swagger with Basic Authentication
 */
//TODO auth basic configuration
@Suppress("UNUSED")
@Configuration
@EnableSwagger2
@ConditionalOnBean(BasicWebSecurity::class)
class BasicSwaggerConf(
    private val swaggerApiInfo: SwaggerApiInfo
) {

    private val log by LoggerDelegate()

    @Bean
    fun adminApi(): Docket {

        log.info("Swagger security type: [basic] with adminApi()")

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
            .ignoredParameterTypes(*ignoredParameters())
    }

    @Bean
    fun auditApi(): Docket {

        log.info("Swagger security type: [basic] with auditApi()")

        return Docket(DocumentationType.SWAGGER_2)
            .groupName("Auditing")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController::class.java))
            .paths(
                Predicates.or(
                    PathSelectors.ant(SecurityPattern.JAVERS_PATH.antPath()),
                    PathSelectors.ant(SecurityPattern.WEBAUDIT_PATH.antPath())
                )
            )
            .build()
            .ignoredParameterTypes(*ignoredParameters())
    }

    @Bean
    fun businessApi(): Docket {

        log.info("Swagger security type: [basic] with businessApi()")

        return Docket(DocumentationType.SWAGGER_2)
            .groupName("Business")
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController::class.java))
            .paths(
                Predicates.and(
                    Predicates.not(PathSelectors.ant(ROLE_PATH.antPath())),
                    Predicates.not(PathSelectors.ant(PRIVILEGE_PATH.antPath())),
                    Predicates.not(PathSelectors.ant(USER_PATH.antPath())),
                    Predicates.not(PathSelectors.ant(SecurityPattern.JAVERS_PATH.antPath())),
                    Predicates.not(PathSelectors.ant(SecurityPattern.WEBAUDIT_PATH.antPath()))
                )
            ).build()
            .ignoredParameterTypes(*ignoredParameters())
    }
}

