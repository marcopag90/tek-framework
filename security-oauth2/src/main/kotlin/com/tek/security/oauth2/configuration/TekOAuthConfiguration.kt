package com.tek.security.oauth2.configuration

import com.tek.core.TEK_CORE_CONFIGURATION
import com.tek.core.TekModuleConfiguration
import com.tek.core.configuration.TekCoreConfiguration
import com.tek.core.util.LoggerDelegate
import com.tek.security.oauth2.TEK_OAUTH_CONFIGURATION
import com.tek.security.oauth2.TekOAuthProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn

@Configuration(TEK_OAUTH_CONFIGURATION)
@DependsOn(TEK_CORE_CONFIGURATION)
@ConditionalOnBean(TekCoreConfiguration::class)
class TekOAuthConfiguration(
    private val properties: TekOAuthProperties
) : TekModuleConfiguration<TekOAuthConfiguration>(TekOAuthConfiguration::class.java) {

    companion object {
        private val log by LoggerDelegate()
    }

    override fun checkModuleConfiguration() {
    }
}