package it.jbot.security.data

import it.jbot.security.model.OAuth2Client
import it.jbot.security.repository.OAuth2ClientRepository
import it.jbot.shared.ifNull
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class OAuth2ClientDataRunner(
    private val oAuth2ClientRepository: OAuth2ClientRepository,
    private val jBotPasswordEncoder: BCryptPasswordEncoder
) : CommandLineRunner {
    
    override fun run(vararg args: String?) {
        
        if (!oAuth2ClientRepository.findById("clientId").isPresent) {
            
            oAuth2ClientRepository.save(OAuth2Client().apply {
                clientId = "clientId"
                clientSecret = jBotPasswordEncoder.encode("secret")
                scope = "read,write"
                authorizedGrantTypes = "password,refresh_token,client_credentials"
                authorities = "ROLE_CLIENT"
                accessTokenValidity = 300
            })
        }
    }
}