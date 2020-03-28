package com.tek.core

import com.tek.core.TekRunnerProperties.TekRunnerAction
import com.tek.core.util.LabelEnum
import com.tek.core.util.LoggerDelegate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.core.env.Environment
import javax.naming.ConfigurationException

/**
 * Tek implementation of [CommandLineRunner].
 */
abstract class TekDataRunner<Runner>(
    coreProperties: TekCoreProperties,
    private val runner: Class<Runner>
) : CommandLineRunner {

    @Autowired
    lateinit var environment: Environment

    private val log by LoggerDelegate()

    private val action = coreProperties.runner.action

    private enum class Profile(override val label: String) : LabelEnum {
        DEVELOPMENT(SpringProfile.DEVELOPMENT),
        PRODUCTION(SpringProfile.PRODUCTION)
    }

    override fun run(vararg args: String?) {
        environment.activeProfiles.map { profile ->
            when (profile) {
                SpringProfile.DEVELOPMENT -> logAndAct(Profile.DEVELOPMENT)
                SpringProfile.PRODUCTION -> logAndAct(Profile.PRODUCTION)
                else -> ConfigurationException(
                    """
                        There must be a profile ${SpringProfile.DEVELOPMENT} or ${SpringProfile.PRODUCTION} active
                        to execute [${runner.simpleName}]!
                    """.trimIndent()
                )
            }
        }
    }

    private fun logAndAct(profile: Profile) {
        log.info(
            "Executing {} with profile: {} and action: {}",
            runner.simpleName,
            profile.name,
            action
        )
        return when (action) {
            TekRunnerAction.CREATE -> when (profile) {
                Profile.DEVELOPMENT -> runDevelopmentMode()
                Profile.PRODUCTION -> runProductionMode()
            }
            TekRunnerAction.NONE -> log.info("Skipping ", runner.simpleName)
        }
    }

    abstract fun runDevelopmentMode()

    protected open fun runProductionMode() {
        log.info(
            "No configuration provided for profile {}. Skipping {}",
            Profile.PRODUCTION,
            runner.simpleName
        )
    }
}