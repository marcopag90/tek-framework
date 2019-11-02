package it.jbot.security.oauth.data

import it.jbot.security.JBotPasswordEncoder
import it.jbot.security.oauth.OAuthConstant.DEFAULT_CLIENT_ID
import it.jbot.security.oauth.OAuthConstant.DEFAULT_CLIENT_SECRET
import it.jbot.security.oauth.OAuthConstant.DEFAULT_GRANTS
import it.jbot.security.oauth.OAuthConstant.DEFAULT_RESOURCE_ID
import it.jbot.security.oauth.OAuthConstant.DEFAULT_SCOPES
import it.jbot.security.oauth.configuration.JBotOAuthWebSecurity
import it.jbot.security.oauth.model.ClientDetails
import it.jbot.security.oauth.repository.ClientDetailsRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Component

@Component
@ConditionalOnBean(JBotOAuthWebSecurity::class)
class ClientDetailsDataRunner(
    private val oAuth2ClientRepository: ClientDetailsRepository,
    private val jBotPasswordEncoder: JBotPasswordEncoder
) : CommandLineRunner {
    
    val clientId = DEFAULT_CLIENT_ID
    val clientSecret = DEFAULT_CLIENT_SECRET
    val resourceId = DEFAULT_RESOURCE_ID
    val scope = DEFAULT_SCOPES
    val authorizedGrantTypes = DEFAULT_GRANTS
    
    override fun run(vararg args: String?) {
        
        if (!oAuth2ClientRepository.findById(clientId).isPresent) {
            
            oAuth2ClientRepository.save(ClientDetails().apply {
                
                clientId = this@ClientDetailsDataRunner.clientId
                clientSecret = jBotPasswordEncoder.encoder()
                    .encode(this@ClientDetailsDataRunner.clientSecret)
                
                resourceId = this@ClientDetailsDataRunner.resourceId
                
                scope = this@ClientDetailsDataRunner.scope
                authorizedGrantTypes =
                    this@ClientDetailsDataRunner.authorizedGrantTypes
                authorities = "ROLE_CLIENT"
                
                accessTokenValidity = 3600
                refreshTokenValidity = 3600
            })
        }
    }
}