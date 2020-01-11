package com.tek.core.configuration

import com.tek.core.SpringProfile.DEVELOPMENT
import com.tek.core.SpringProfile.PRODUCTION
import com.tek.core.util.LoggerDelegate
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.ConfigurableEnvironment
import javax.annotation.PostConstruct

/**
 * Base configuration to check Active Profile at runtime, after enviroment has been injected.
 *
 * If no profile is found, set default profile to _dev_
 */
@Configuration
class TekCoreConfiguration(
    private val env: ConfigurableEnvironment
) {

    private val log = LoggerFactory.getLogger(TekCoreConfiguration::class.java)

    @PostConstruct
    fun checkActiveProfile() {

        if (env.activeProfiles.isEmpty()) {
            log.warn("No Spring profile configured, running with default profile: $DEVELOPMENT")
            env.setActiveProfiles(DEVELOPMENT)
        } else {
            val activeProfiles = env.activeProfiles
            log.info("Running with Spring profile(s): ${activeProfiles.contentToString()}")
            if (activeProfiles.contains(DEVELOPMENT) && activeProfiles.contains(PRODUCTION)) {
                log.error("You have misconfigured your application! It should not run with both $DEVELOPMENT and $PRODUCTION profiles at the same time!")
            }
        }
    }
}