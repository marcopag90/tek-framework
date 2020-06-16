package com.tek.security.common

import com.tek.core.LOCALE_PATH
import com.tek.core.TEST_PATH

/**Class sharing constants among all Spring Security configurations*/
object TekSecurityPattern {

    fun unauthenticatedPatterns(): Array<String> {
        return arrayOf(
            INDEX_PATH,
            REGISTER_PATH,
            LOCALE_PATH,
            "$TEST_PATH/**"
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
            "/configuration/ui/**",
            "/swagger-resources/**",
            "/configuration/security/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/**"
        )
    }
}
