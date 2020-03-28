package com.tek.security.common

import com.tek.core.LOCALE_PATH

/**Class sharing constants among all Spring Security configurations*/
object TekSecurityPattern {

    fun unauthenticatedPatterns(): Array<String> {
        return arrayOf(
            INDEX_PATH,
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
