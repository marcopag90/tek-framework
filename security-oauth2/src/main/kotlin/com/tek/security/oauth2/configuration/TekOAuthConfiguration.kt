package com.tek.security.oauth2.configuration

import com.tek.core.TekModuleConfiguration
import com.tek.core.util.LoggerDelegate
import com.tek.security.common.TEK_SECURITY_CONFIGURATION
import com.tek.security.common.configuration.TekSecurityConfiguration
import com.tek.security.oauth2.TEK_OAUTH_CONFIGURATION
import com.tek.security.oauth2.TekOAuthProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn

@Configuration(TEK_OAUTH_CONFIGURATION)
@DependsOn(TEK_SECURITY_CONFIGURATION)
@ConditionalOnBean(TekSecurityConfiguration::class)
class TekOAuthConfiguration(
    private val properties: TekOAuthProperties
) : TekModuleConfiguration<TekOAuthConfiguration>(TekOAuthConfiguration::class.java) {

    companion object {
        private val log by LoggerDelegate()
    }

    override fun checkModuleConfiguration() {
    }
}