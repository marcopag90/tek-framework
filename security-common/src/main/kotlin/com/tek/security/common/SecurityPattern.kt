package com.tek.security.common

import com.tek.core.i18n.LOCALE_PATH

/**Class sharing constants among all Spring Security configurations*/
object SecurityPattern {

    const val BASE_PATH = "/"
    const val REGISTER_PATH = "/register"
    const val ROLE_PATH = "/role"
    const val PRIVILEGE_PATH = "/privilege"
    const val USER_PATH = "/user"
    const val PREFERENCES_PATH = "/preferences"
    const val JAVERS_PATH = "/javers"
    const val WEBAUDIT_PATH = "/webaudit" //TODO web audit controller
    const val NOTIFICATION_PATH = "/notification"

    fun unauthenticatedPatterns(): Array<String> {
        return arrayOf(
            BASE_PATH,
            REGISTER_PATH,
            LOCALE_PATH
        )
    }

    fun clientResources(): Array<String> {
        return arrayOf(
            "/*jpg", "/*png", "/*css", "/*js", "/*ico", "/webjars/**", "/assets/**"
        )
    }

    fun nebularResources(): Array<String> {
        return arrayOf(
            "nebular.ttf?4ozerq",
            "nebular.woff?4ozerq",
            "/*woff",
            "/*woff2",
            "/*ttf"
        )
    }

    fun swaggerResources(): Array<String> {
        return arrayOf(
            "/v2/api-docs",
            "/v3/**",
            "/com.tek.security.oauth2.configuration/ui/**",
            "/swagger-resources/**",
            "/com.tek.security.oauth2.configuration/security/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/**"
        )
    }
}
