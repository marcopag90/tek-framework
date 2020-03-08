package com.tek.core

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.util.ClassUtils
import javax.annotation.PostConstruct

@Suppress("UNUSED")
abstract class TekModuleConfiguration<Configuration>(
    private val configuration: Class<Configuration>
) {

    @Autowired
    lateinit var environment: ConfigurableEnvironment

    private val log = LoggerFactory.getLogger(TekModuleConfiguration::class.java)

    @PostConstruct
    fun postConstruct() {
        log.info("Checking Tek Modules Configuration: [${ClassUtils.getUserClass(configuration).simpleName}]")
        checkModuleConfiguration()
    }

    abstract fun checkModuleConfiguration()
}