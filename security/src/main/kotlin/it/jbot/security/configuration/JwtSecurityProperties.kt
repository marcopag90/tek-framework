package it.jbot.security.configuration

import it.jbot.security.SecurityConstant.jwtSecurity
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.io.Resource

@ConfigurationProperties(jwtSecurity)
class JwtSecurityProperties {
    
    var jwt: JwtProperties? = null
    
    class JwtProperties {
        
        var keyStore: Resource? = null
        var keyStorePassword: String? = null
        var keyPairAlias: String? = null
        var keyPairPassword: String? = null
        var publicKey: Resource? = null
    }
}


