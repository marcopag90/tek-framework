package com.tek.core

import org.slf4j.LoggerFactory
import org.springframework.core.env.ConfigurableEnvironment
import javax.annotation.PostConstruct

@Suppress("UNUSED")
abstract class TekModuleConfiguration(
    protected val environment: ConfigurableEnvironment
) {

    private val log = LoggerFactory.getLogger(TekModuleConfiguration::class.java)

    @PostConstruct
    fun postConstruct() {
        log.info("Checking Tek Modules Configurations...")
        checkModuleConfiguration()
    }

    abstract fun checkModuleConfiguration()
}