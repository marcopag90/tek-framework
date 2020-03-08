package com.tek.core.data

import com.tek.core.SpringProfile
import com.tek.core.TekCoreProperties
import com.tek.core.TekRunnerAction
import com.tek.core.util.LabelEnum
import com.tek.core.util.LoggerDelegate
import org.springframework.boot.CommandLineRunner
import org.springframework.core.env.Environment
import javax.naming.ConfigurationException

/**
 * Tek implementation of [CommandLineRunner].
 *
 * Extend this class if you need to provide a custom behaviour for a production environment.
 */
abstract class TekDataRunner(
    private val environment: Environment,
    coreProperties: TekCoreProperties
) : CommandLineRunner {

    private enum class Profile(override val label: String) : LabelEnum {
        DEVELOPMENT(SpringProfile.DEVELOPMENT),
        PRODUCTION(SpringProfile.PRODUCTION)
    }

    private val log by LoggerDelegate()

    private val action = coreProperties.runner.action

    override fun run(vararg args: String?) {

        environment.activeProfiles.map { profile ->
            when (profile) {
                SpringProfile.DEVELOPMENT -> logAndAct(Profile.DEVELOPMENT)
                SpringProfile.PRODUCTION -> logAndAct(Profile.PRODUCTION)
                else -> ConfigurationException("There must be a profile: ${SpringProfile.DEVELOPMENT} or ${SpringProfile.PRODUCTION} active to execute this data runner!")
            }
        }
    }

    private fun logAndAct(profile: Profile) {
        log.info("Executing data runner with profile: [${profile.name}] and action: [$action]")
        return when (action) {
            TekRunnerAction.CREATE -> when (profile) {
                Profile.DEVELOPMENT -> runDevelopmentMode()
                Profile.PRODUCTION -> runProductionMode()
            }
            TekRunnerAction.NONE -> log.info("${TekRunnerAction.NONE.name} found. Skipping $this!")
        }
    }

    abstract fun runDevelopmentMode()

    protected open fun runProductionMode() {
        log.info("No com.tek.security.oauth2.configuration provided for ${Profile.PRODUCTION.name}. Skipping $this!")
    }
}