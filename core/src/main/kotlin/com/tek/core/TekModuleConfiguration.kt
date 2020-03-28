package com.tek.core

import com.tek.core.util.LoggerDelegate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.util.ClassUtils
import javax.annotation.PostConstruct

abstract class TekModuleConfiguration<Configuration>(
    private val configuration: Class<Configuration>
) {

    @Autowired
    lateinit var environment: ConfigurableEnvironment

    companion object {
        private val log by LoggerDelegate()
    }

    @PostConstruct
    fun postConstruct() {
        log.info(
            "Checking Tek Modules Configuration: {}",
            ClassUtils.getUserClass(configuration).simpleName
        )
        checkModuleConfiguration()
    }

    abstract fun checkModuleConfiguration()
}