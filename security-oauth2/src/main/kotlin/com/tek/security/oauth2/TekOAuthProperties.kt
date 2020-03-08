package com.tek.security.oauth2

import com.tek.core.util.TekDurationProperty
import com.tek.core.util.TekProperty
import com.tek.security.oauth2.configuration.OAuthWebSecurity
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.convert.DurationUnit
import org.springframework.context.annotation.Configuration
import java.time.temporal.ChronoUnit
import kotlin.properties.Delegates

@Suppress("UNUSED")
@Configuration
@ConditionalOnBean(OAuthWebSecurity::class)
@ConfigurationProperties(prefix = "tek.security.module.oauth2")
class TekOAuthProperties {
    val client = ClientDetailsProperties()
    var accessTokenHost: TekProperty by Delegates.notNull()
}

/**
 * Configuration lookup for oauth_client_details table.
 */
class ClientDetailsProperties {

    /**
     * Matching oauth_client_details.client_id to authorize a _tek client_
     */
    var clientId: TekProperty by Delegates.notNull()

    /**
     * Matching oauth_client_details.client_secret to authorize access to a resource for a _tek client_ with given _secret_
     */
    var clientSecret: TekProperty by Delegates.notNull()

    /**
     * Matching oauth_client_details.resource_ids to authorize access to a resource for a _tek client_
     */
    var resourceId: TekProperty by Delegates.notNull()

    /**
     * Matching oauth_client_details.authorities to let client inquiry the following paths:
     *
     * _oauth/check_token_
     *
     * _oauth/token_key_ (if public key is available)
     */
    var authority: TekProperty by Delegates.notNull()

    /**
     * Matching oauth_client_details.authorized_grant_types
     */
    var grants: TekProperty by Delegates.notNull()

    /**
     * Matching oauth_client_details.scope
     */
    var scope: TekProperty by Delegates.notNull()

    /**
     * Access Token Validity in _seconds_
     */
    @DurationUnit(ChronoUnit.SECONDS)
    var accessTokenValidity: TekDurationProperty? = null

    /**
     * Refresh Token Validity in _seconds_
     */
    @DurationUnit(ChronoUnit.SECONDS)
    var refreshTokenValidity: TekDurationProperty? = null
}