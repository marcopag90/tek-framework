package com.tek.core

import com.tek.core.configuration.TekCorsFilter
import com.tek.core.util.LabelEnum
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

    override fun toString(): String = """TekCoreProperties:
tek.core.module:
    cors:
        $cors, 
    runner:
        $runner
""".trimIndent()
}


/**
 * Configuration lookup for [TekCorsFilter].
 *
 * Default configuration is provided if none is found (development purpose only)
 *
 */
class TekCorsProperties {

    var allowedOrigin: TekProperty by Delegates.notNull()

    var allowedCredentials: TekProperty by Delegates.notNull()

    var allowedMethods: Array<TekProperty> by Delegates.notNull()

    var allowedHeaders: Array<TekProperty> by Delegates.notNull()

    override fun toString(): String = """allowedOrigin: $allowedOrigin,
        allowedCredentials: $allowedCredentials,
        allowedMethods: ${allowedMethods.toList()},
        allowedHeaders: ${allowedHeaders.toList()}
    """.trimIndent()
}

/**
 * Configuration for Tek Data Runners.
 */
class TekRunnerProperties {
    var action: TekRunnerAction? = TekRunnerAction.NONE

    override fun toString(): String = """
            action: $action
    """.trimIndent()
}

enum class TekRunnerAction(override val label: String) : LabelEnum {

    CREATE("Execute data runner every time"),
    NONE("No action is taken")
}


