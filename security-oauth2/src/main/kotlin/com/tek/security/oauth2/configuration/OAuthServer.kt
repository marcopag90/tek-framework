package com.tek.security.oauth2.configuration

import com.tek.core.util.or
import com.tek.security.common.service.TekAuthService
import com.tek.security.common.hasAuthority
import com.tek.security.common.isAnonymous
import com.tek.security.oauth2.TekOAuthProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore
import javax.sql.DataSource

/**OAuth2 Authorization Server Configuration
 *
 * to manage clientId and token validation
 */
@Configuration
@ConditionalOnBean(OAuthWebSecurity::class)
@EnableAuthorizationServer
class OAuthServer(
    private val datasource: DataSource,
    private val authenticationManager: AuthenticationManager,
    private val tekAuthService: TekAuthService,
    private val properties: TekOAuthProperties
) : AuthorizationServerConfigurerAdapter() {

    @Bean
    fun tokenStore() = JdbcTokenStore(datasource)

    /**
     * Token Service to allow management of user token
     */
    @Bean
    fun tokenServices(): DefaultTokenServices = DefaultTokenServices().apply {
        this.setTokenStore(tokenStore())
        this.setSupportRefreshToken(true)
    }

    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.jdbc(datasource)
    }

    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        security
            // unauthenticated access to path: oauth/token with Basic Authentication to get a Bearer Token
            .tokenKeyAccess(
                isAnonymous().or(
                    hasAuthority(properties.client.authority)
                ))
            // authenticated access to path: oauth/check_token with Basic Authentication (clientId and clientSecret) to get token status
            .checkTokenAccess(hasAuthority(properties.client.authority))
            .passwordEncoder(tekAuthService.passwordEncoder())
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints
            .tokenStore(tokenStore())
            .authenticationManager(authenticationManager)
            .userDetailsService(tekAuthService)
    }
}
