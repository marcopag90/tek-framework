package it.jbot.security.oauth.data

import it.jbot.security.JBotPasswordEncoder
import it.jbot.security.oauth.configuration.ClientDetailsProperties
import it.jbot.security.oauth.configuration.OAuthWebSecurity
import it.jbot.security.oauth.model.ClientDetails
import it.jbot.security.oauth.repository.ClientDetailsRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Component

@Component
@ConditionalOnBean(OAuthWebSecurity::class)
class ClientDetailsDataRunner(
    private val properties: ClientDetailsProperties,
    private val oAuth2ClientRepository: ClientDetailsRepository,
    private val jBotPasswordEncoder: JBotPasswordEncoder
) : CommandLineRunner {
    
    override fun run(vararg args: String?) {
        
        if (!oAuth2ClientRepository.findById(properties.clientId).isPresent) {
            
            oAuth2ClientRepository.save(ClientDetails().apply {
                
                this.clientId = properties.clientId
                this.clientSecret = jBotPasswordEncoder.encoder()
                    .encode(properties.clientSecret)
                this.resourceId = properties.resourceId
                this.scope = properties.scope
                this.authorizedGrantTypes = properties.grants
                this.authorities = properties.authority
                this.accessTokenValidity = properties.accessTokenValidity.seconds.toInt()
                this.refreshTokenValidity = properties.refreshTokenValidity.seconds.toInt()
            })
        }
    }
}