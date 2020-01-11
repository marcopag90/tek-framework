package com.tek.security.data

import com.tek.core.SpringProfile
import com.tek.core.TekProperties
import com.tek.core.data.TekRunnerAction
import com.tek.core.util.LoggerDelegate
import com.tek.core.util.unreachableCode
import org.springframework.boot.CommandLineRunner
import org.springframework.core.env.Environment

/**
 * Tek implementation of [CommandLineRunner]
 */
abstract class TekSecurityDataRunner(
    private val environment: Environment,
    properties: TekProperties
) : CommandLineRunner {

    private val log by LoggerDelegate()

    private val action = properties.runner!!.action

    abstract fun runDevelopmentMode()

    abstract fun runProductionMode()

    override fun run(vararg args: String?) {

        environment.activeProfiles.map { profile ->
            when (profile) {
                SpringProfile.DEVELOPMENT -> initDevelopmentMode(profile)
                SpringProfile.PRODUCTION -> initProductionMode(profile)
                else -> unreachableCode()
            }
        }
    }

    private fun logConfiguration(profile: String) =
        log.info("Executing data runner with profile: [$profile] and action: [$action]")

    private fun initDevelopmentMode(profile: String) {
        logConfiguration(profile)
        when (action) {
            TekRunnerAction.CREATE -> runDevelopmentMode()
            TekRunnerAction.NONE -> log.info("Skipping data runner!")
        }
    }

    private fun initProductionMode(profile: String) {
        logConfiguration(profile)
        when (action) {
            TekRunnerAction.CREATE -> runProductionMode()
            TekRunnerAction.NONE -> log.info("Skipping data runner!")
        }
    }
}