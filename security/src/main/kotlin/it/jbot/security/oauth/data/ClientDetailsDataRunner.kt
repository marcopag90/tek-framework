package it.jbot.security.oauth.data

import it.jbot.security.JBotPasswordEncoder
import it.jbot.security.oauth.configuration.JBotOAuthWebSecurity
import it.jbot.security.oauth.model.ClientDetails
import it.jbot.security.oauth.repository.ClientDetailsRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
@ConditionalOnBean(JBotOAuthWebSecurity::class)
class ClientDetailsDataRunner(
    private val oAuth2ClientRepository: ClientDetailsRepository,
    private val jBotPasswordEncoder: JBotPasswordEncoder
) : CommandLineRunner {
    
    override fun run(vararg args: String?) {
        
        if (!oAuth2ClientRepository.findById("clientId").isPresent) {
            
            oAuth2ClientRepository.save(ClientDetails().apply {
                clientId = "clientId"
                clientSecret = jBotPasswordEncoder.encoder().encode("secret")
                scope = "read,write"
                authorizedGrantTypes =
                    "password,refresh_token,client_credentials"
                authorities = "ROLE_CLIENT"
                accessTokenValidity = 300
            })
        }
    }
}