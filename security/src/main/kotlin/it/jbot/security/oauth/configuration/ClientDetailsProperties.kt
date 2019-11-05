package it.jbot.security.oauth.configuration

import it.jbot.shared.LoggerDelegate
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.convert.DurationUnit
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import java.time.Duration
import java.time.temporal.ChronoUnit

/**
 * Configuration lookup for oauth_client_details table.
 *
 * security.properties file _MUST_ exists with its own profile (eg. security-dev.properties)
 *
 * security properties variables _MUST_ start with _client_ prefix (eg. client.clientId)
 *
 */
@Configuration
@ConditionalOnBean(JBotOAuthWebSecurity::class)
@PropertySource(
    "classpath:security-\${spring.profiles.active}.properties",
    ignoreResourceNotFound = true
)
@ConfigurationProperties("client")
class ClientDetailsProperties(
    environment: Environment
) {
    
    private val logger by LoggerDelegate()
    
    init {
        logger.info("Evaluating security.properties with profile: ${environment.activeProfiles[0]}")
    }
    
    /**
     * Matching oauth_client_details.client_id to authorize a _jbot client_
     */
    var clientId: String = "jBotClientId"
    
    /**
     * Matching oauth_client_details.client_secret to authorize access to a resource for a _jbot client_ with given _secret_
     */
    var clientSecret: String = "jBotSecret"
    
    /**
     * Matching oauth_client_details.resource_ids to authorize access to a resource for a _jbot client_
     */
    var resourceId: String = "jBotResourceId"
    
    /**
     * Matching oauth_client_details.authorities to let client inquiry the following paths:
     *
     * _oauth/check_token_
     * _oauth/token_key_ (if public key is available)
     */
    var authority: String = "ROLE_CLIENT"
    
    /**
     * Matching oauth_client_details.authorized_grant_types
     */
    var grants: String = "password,refresh_token,client_credentials"
    
    /**
     * Matching oauth_client_details.scope
     */
    var scope: String = "read,write"
    
    /**
     * Access Token Validity in _seconds_
     */
    @DurationUnit(ChronoUnit.SECONDS)
    var accessTokenValidity: Duration = Duration.ofSeconds(300)
    
    /**
     * Refresh Token Validity in _seconds_
     */
    @DurationUnit(ChronoUnit.SECONDS)
    var refreshTokenValidity: Duration = Duration.ofSeconds(600)
}