package it.jbot.security

/**Class sharing constants among all Spring Security configurations*/
object SecurityPattern {

    const val BASE_PATTERN = "/"
    const val REGISTER_PATTERN = "/register"

    fun unauthenticatedPatterns(): Array<String> {
        return arrayOf(
            BASE_PATTERN,
            REGISTER_PATTERN
        )
    }

    fun clientResources(): Array<String> {
        return arrayOf(
            "/*jpg", "/*png", "/*css", "/*js", "/*ico", "/webjars/**"
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
            "/v2/api-docs", "/component/**", "/swagger*/**"
        )
    }
}
