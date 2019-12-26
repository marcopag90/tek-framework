package it.jbot.core

import it.jbot.core.SpringProfile.DEVELOPMENT
import it.jbot.core.SpringProfile.PRODUCTION
import it.jbot.core.util.LoggerDelegate
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.ConfigurableEnvironment
import javax.annotation.PostConstruct

/**
 * Base configuration to check Active Profile at runtime, after enviroment has been injected.
 *
 * If no profile is found, set default profile to _dev_
 */
@Configuration
class JBotCoreConfiguration(
    private val env: ConfigurableEnvironment
) {

    private val log by LoggerDelegate()

    @PostConstruct
    fun checkActiveProfile() {

        if (env.activeProfiles.isEmpty()) {
            log.warn("No Spring profile configured, running with default profile: $DEVELOPMENT")
            env.setActiveProfiles(DEVELOPMENT)
        } else {
            val activeProfiles = env.activeProfiles
            log.info("Running with Spring profile(s) : ${activeProfiles.contentToString()}")
            if (activeProfiles.contains(DEVELOPMENT) && activeProfiles.contains(PRODUCTION)) {
                log.error("You have misconfigured your application! It should not run with both $DEVELOPMENT and $PRODUCTION profiles at the same time!")
            }
        }
    }
}