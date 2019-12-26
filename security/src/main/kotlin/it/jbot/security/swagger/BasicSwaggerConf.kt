package it.jbot.security.swagger

import it.jbot.core.swagger.SwaggerIgnore.ignoredParameters
import it.jbot.core.util.LoggerDelegate
import it.jbot.security.configuration.BasicWebSecurity
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
@Configuration
@EnableSwagger2
@ConditionalOnBean(BasicWebSecurity::class)
class BasicSwaggerConf {

    private val log by LoggerDelegate()

    @Bean
    fun api(): Docket {

        log.info("Swagger security type: [basic]")

        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController::class.java))
            .paths(PathSelectors.any())
            .build()
            .ignoredParameterTypes(*ignoredParameters());
    }
}

