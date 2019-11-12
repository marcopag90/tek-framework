package it.jbot.security

/**Class sharing constants among all Spring Security configurations*/
object SecurityConstant {
    
    const val DEFAULT_SECURED_PATTERN = "/**"
    const val REGISTER_PATTERN = "/register"
    
    //TODO split into single val (kotlin can't assign constant to non-primitive types
    val clientResources =
        arrayOf("/*jpg", "/*png", "/*css", "/*js", "/*ico", "/webjars/**")
    val entryPointResources = arrayOf("/", "/login", "/register")
    val swaggerResources =
        arrayOf("/v2/api-docs", "/component/**", "/swagger*/**")
    
}
