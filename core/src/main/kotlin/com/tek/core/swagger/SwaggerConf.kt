package com.tek.core.swagger

import com.tek.core.swagger.SwaggerIgnore.ignoredParameters
import com.tek.core.util.ConditionalOnMissingProperty
import com.tek.core.util.LoggerDelegate
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 * Configuration for Swagger without security module
 */
@Configuration
@EnableSwagger2
@ConditionalOnMissingProperty(value = "security.type")
class SwaggerConf(
    private val swaggerApiInfo: SwaggerApiInfo
) {

    private val log = LoggerFactory.getLogger(SwaggerConf::class.java)

    @Bean
    fun api(): Docket {

        log.info("Swagger security type: [no security]")

        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController::class.java))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(swaggerApiInfo.tekApiInfo())
            .ignoredParameterTypes(*ignoredParameters())
    }
}

@Configuration
class SwaggerApiInfo(
    private val properties: SwaggerApiInfoProperties
) {
    @Bean
    fun tekApiInfo(): ApiInfo =
        ApiInfo(
            properties.title,
            properties.description,
            resolveVersion(),
            properties.termOfServiceUrl,
            Contact(
                properties.contactName,
                properties.contactUrl,
                properties.contactEmail
            ),
            properties.license,
            properties.licenseUrl,
            listOf()
        )

    //TODO resolve version
    private fun resolveVersion(): String? {
        return null
    }

}

