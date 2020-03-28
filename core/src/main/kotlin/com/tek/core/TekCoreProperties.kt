package com.tek.core

import com.tek.core.configuration.TekCorsFilter
import com.tek.core.util.LabelEnum
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.config.annotation.InterceptorRegistry

@Configuration
@ConfigurationProperties(prefix = TEK_MODULE_CORE)
class TekCoreProperties {
    val cors: TekCorsProperties = TekCorsProperties()
    val runner: TekRunnerProperties = TekRunnerProperties()
    val locale: TekLocaleProperties = TekLocaleProperties()
}

/**
 * Configuration properties for [TekCorsFilter].
 *
 * Fallback to default configuration if none provided.
 */
class TekCorsProperties {
    internal enum class RequestHeader(
        override val label: String
    ) : LabelEnum {
        X_REQUESTED_WITH("x-requested-with"),
        L_AUTHORIZATION("authorization"),
        CONTENT_TYPE("Content-Type"),
        U_AUTHORIZATION("Authorization"),
        CREDENTIAL("credential"),
        X_XSRF_TOKEN("X-XSRF-TOKEN")
    }

    var allowedOrigin: String = "http://localhost:4200"
    var allowedCredentials: String = "true"
    var allowedMethods: Array<String> =
        RequestMethod.values().map { it.name }.toTypedArray()
    var allowedHeaders: Array<String> =
        RequestHeader.values().map { it.label }.toTypedArray()
}

/**
 * Configuration properties for [TekDataRunner].
 */
class TekRunnerProperties {
    enum class TekRunnerAction { CREATE, NONE }

    var action: TekRunnerAction = TekRunnerAction.NONE
}

/**
 * Configuration properties for [InterceptorRegistry.addInterceptor] to manage app locale resolution.
 *
 * Fallback to default configuration if none provided.
 */
class TekLocaleProperties {
    /** Session, Cookie */
    enum class TekLocaleType { SESSION, COOKIE, ACCEPT_HEADER }

    var type: TekLocaleType = TekLocaleType.SESSION
    var cookieName: String = "locale"
    var cookieMaxAge: Int = 24 * 60 * 60
    var interceptorName: String = "lang"
}





