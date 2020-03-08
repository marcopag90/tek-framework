package com.tek.core

import com.tek.core.configuration.TekCorsFilter
import com.tek.core.util.TekProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import kotlin.properties.Delegates

@Suppress("UNUSED")
@Configuration
@ConfigurationProperties(prefix = "tek.core.module")
class TekCoreProperties {
    val cors: TekCorsProperties = TekCorsProperties()
    val runner: TekRunnerProperties = TekRunnerProperties()
    val locale: TekLocaleProperties = TekLocaleProperties()
}


/**
 * Configuration lookup for [TekCorsFilter].
 *
 * Default com.tek.security.oauth2.configuration is provided if none is found (development purpose only)
 *
 */
class TekCorsProperties {

    var allowedOrigin: TekProperty by Delegates.notNull()

    var allowedCredentials: TekProperty by Delegates.notNull()

    var allowedMethods: Array<TekProperty> by Delegates.notNull()

    var allowedHeaders: Array<TekProperty> by Delegates.notNull()
}

/**
 * Configuration for Tek Data Runners.
 */
class TekRunnerProperties {
    var action: TekRunnerAction = TekRunnerAction.NONE
}

class TekLocaleProperties {
    var type: TekLocaleType by Delegates.notNull()
}

/**
 * Configuration for TekLocale management
 */
enum class TekLocaleType {
    SESSION, COOKIE, ACCEPT_HEADER
}

enum class TekRunnerAction {
    CREATE, NONE
}


