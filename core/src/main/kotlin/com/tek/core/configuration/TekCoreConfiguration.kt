package com.tek.core.configuration

import com.tek.core.SpringProfile.DEVELOPMENT
import com.tek.core.SpringProfile.PRODUCTION
import com.tek.core.SpringProfile.TEST
import com.tek.core.TekCoreProperties
import com.tek.core.TekModuleConfiguration
import com.tek.core.swagger.SwaggerApiInfo
import com.tek.core.swagger.SwaggerApiInfoProperties
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.ConfigurableEnvironment
import javax.naming.ConfigurationException

const val TEK_CORE_CONFIGURATION = "TekCoreConfiguration"

/**
 * Core Module Configuration to check
 *
 * - Active Profile at runtime, after enviroment has been injected
 * - [TekCoreProperties] properties
 * - [SwaggerApiInfo] properties
 */
@Suppress("unused")
@Configuration(TEK_CORE_CONFIGURATION)
class TekCoreConfiguration(
    private val properties: TekCoreProperties,
    private val swaggerProperties: SwaggerApiInfoProperties
) : TekModuleConfiguration<TekCoreConfiguration>(TekCoreConfiguration::class.java) {

    private val log = LoggerFactory.getLogger(TekCoreConfiguration::class.java)

    override fun checkModuleConfiguration() {
        checkActiveProfiles()
    }

    private fun checkActiveProfiles() {
        if (environment.activeProfiles.none { it == DEVELOPMENT || it == TEST || it == PRODUCTION })
            throw ConfigurationException(
                """
                Spring active profile NOT FOUND!
                Evaluate the property [profiles.active: <some-profile>] in your application.yaml/properties file.
                If the property is evaluated, check your classpath com.tek.security.oauth2.configuration or Maven pom.xml (if you are using resource filtering)
                and try to re-build your project or run Maven with the following goals: clean, compile.
            """.trimIndent()
            )

        val activeProfiles = environment.activeProfiles
        log.info("Running with Spring profile(s): ${activeProfiles.contentToString()}")

        if (activeProfiles.contains(DEVELOPMENT) && activeProfiles.contains(PRODUCTION))
            throw ConfigurationException(
                """
                    You have misconfigured your application! 
                    It should not run with both $DEVELOPMENT and $PRODUCTION profiles at the same time!
                """.trimIndent()
            )
    }
}