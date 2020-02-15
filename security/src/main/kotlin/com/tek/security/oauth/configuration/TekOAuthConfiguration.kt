package com.tek.security.oauth.configuration

import com.tek.core.TekModuleConfiguration
import com.tek.security.oauth.TekOAuthProperties
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.ConfigurableEnvironment

@Suppress("UNUSED")
@Configuration
class TekOAuthConfiguration(
    environment: ConfigurableEnvironment,
    private val properties: TekOAuthProperties
) : TekModuleConfiguration(environment) {

    private val log = LoggerFactory.getLogger(TekOAuthConfiguration::class.java)

    override fun checkModuleConfiguration() {
    }
}