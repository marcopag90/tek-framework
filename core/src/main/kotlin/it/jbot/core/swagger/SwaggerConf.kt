package it.jbot.core.swagger

import it.jbot.core.swagger.SwaggerIgnore.ignoredParameters
import it.jbot.core.util.ConditionalOnMissingProperty
import it.jbot.core.util.LoggerDelegate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 * Configuration for Swagger without security module
 */
@Configuration
@EnableSwagger2
@ConditionalOnMissingProperty(value = "security.type")
class SwaggerConf {

    private val log by LoggerDelegate()

    @Bean
    fun api(): Docket {

        log.info("Swagger security type: [no security]")

        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController::class.java))
            .paths(PathSelectors.any())
            .build()
            .ignoredParameterTypes(*ignoredParameters());
    }
}

