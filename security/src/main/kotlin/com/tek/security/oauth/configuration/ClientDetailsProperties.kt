package com.tek.security.oauth.configuration

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.convert.DurationUnit
import org.springframework.context.annotation.Configuration
import java.time.Duration
import java.time.temporal.ChronoUnit

/**
 * Configuration lookup for oauth_client_details table.
 *
 * Client Properties variables in application.yaml _MUST_ start with _client_ prefix (eg. client.clientId)
 *
 */
@Configuration
@ConditionalOnBean(OAuthWebSecurity::class)
@ConfigurationProperties(prefix = "security.oauth2.client")
class ClientDetailsProperties {
    
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
     *
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