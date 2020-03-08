package com.tek.security.oauth2.configuration

import com.tek.core.TekModuleConfiguration
import com.tek.core.configuration.TEK_CORE_CONFIGURATION
import com.tek.core.configuration.TekCoreConfiguration
import com.tek.security.oauth2.TekOAuthProperties
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn

@Suppress("UNUSED")
@Configuration
@DependsOn(TEK_CORE_CONFIGURATION)
@ConditionalOnBean(TekCoreConfiguration::class)
class TekOAuthConfiguration(
    private val properties: TekOAuthProperties
) : TekModuleConfiguration<TekOAuthConfiguration>(TekOAuthConfiguration::class.java) {

    private val log = LoggerFactory.getLogger(TekOAuthConfiguration::class.java)

    override fun checkModuleConfiguration() {
    }
}