package com.tek.core.configuration

import com.tek.core.SpringProfile.DEVELOPMENT
import com.tek.core.SpringProfile.PRODUCTION
import com.tek.core.SpringProfile.TEST
import com.tek.core.TEK_CORE_CONFIGURATION
import com.tek.core.TekModuleConfiguration
import com.tek.core.util.LoggerDelegate
import org.springframework.context.annotation.Configuration
import javax.naming.ConfigurationException


/**
 * Core Module Configuration to check
 *
 * - Active Profile at runtime, after environment has been injected
 */
@Configuration(TEK_CORE_CONFIGURATION)
class TekCoreConfiguration :
    TekModuleConfiguration<TekCoreConfiguration>(TekCoreConfiguration::class.java) {

    companion object {
        private val log by LoggerDelegate()
    }

    override fun checkModuleConfiguration() = checkActiveProfiles()

    private fun checkActiveProfiles() {
        if (environment.activeProfiles.none { it == DEVELOPMENT || it == TEST || it == PRODUCTION })
            throw ConfigurationException(
                """
                Spring active profile NOT FOUND!
                Evaluate the property [spring.profiles.active: <some-profile>] 
                in your application.yaml/properties file.
                If the property evaluates, check your classpath configuration or Maven pom.xml 
                (if you are using maven resource filtering) and try to re-build your project 
                or run Maven with the following goals: [clean, compile].
            """.trimIndent()
            )

        val activeProfiles = environment.activeProfiles
        log.info("Running with Spring profile(s): {}", activeProfiles.contentToString())

        if (activeProfiles.contains(DEVELOPMENT) && activeProfiles.contains(PRODUCTION))
            throw ConfigurationException(
                """
                    You have misconstrued your application! 
                    It should not run with both $DEVELOPMENT and $PRODUCTION profiles at the same time!
                """.trimIndent()
            )
    }
}