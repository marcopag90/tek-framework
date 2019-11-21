package it.jbot.security

import it.jbot.web.controller.LOCALE_PATTERN

/**Class sharing constants among all Spring Security configurations*/
object SecurityConstant {
    
    const val DEFAULT_SECURED_PATTERN = "/**"
    const val REGISTER_PATTERN = "/register"
    
    fun unauthenticatedPatterns(): Array<String> {
        return arrayOf(
            LOCALE_PATTERN,
            "/user$REGISTER_PATTERN"
        )
    }
    
    fun unauthenticatedResources(): Array<String> {
        return arrayOf(
            "/*jpg", "/*png", "/*css", "/*js", "/*ico", "/webjars/**"
        )
    }
    
    fun swaggerResources() : Array<String> {
        return arrayOf(
            "/v2/api-docs", "/component/**", "/swagger*/**"
        )
    }
}
