package it.jbot.security.oauth.data

import it.jbot.security.JBotPasswordEncoder
import it.jbot.security.oauth.configuration.ClientDetailsProperties
import it.jbot.security.oauth.configuration.JBotOAuthWebSecurity
import it.jbot.security.oauth.model.ClientDetails
import it.jbot.security.oauth.repository.ClientDetailsRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Component

@Component
@ConditionalOnBean(JBotOAuthWebSecurity::class)
class ClientDetailsDataRunner(
    private val properties: ClientDetailsProperties,
    private val oAuth2ClientRepository: ClientDetailsRepository,
    private val jBotPasswordEncoder: JBotPasswordEncoder
) : CommandLineRunner {
    
    override fun run(vararg args: String?) {
        
        if (!oAuth2ClientRepository.findById(properties.clientId).isPresent) {
            
            oAuth2ClientRepository.save(ClientDetails().apply {
                
                clientId = properties.clientId
                clientSecret = jBotPasswordEncoder.encoder()
                    .encode(properties.clientSecret)
                resourceId = properties.resourceId
                scope = properties.scope
                authorizedGrantTypes = properties.grants
                authorities = properties.authority
                accessTokenValidity = properties.accessTokenValidity.seconds.toInt()
                refreshTokenValidity = properties.refreshTokenValidity.seconds.toInt()
            })
        }
    }
}