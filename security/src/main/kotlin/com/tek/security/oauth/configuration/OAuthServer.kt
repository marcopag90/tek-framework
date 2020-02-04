package com.tek.security.oauth.configuration

import com.tek.core.util.TekProperty
import com.tek.core.util.hasAuthority
import com.tek.core.util.isAnonymous
import com.tek.core.util.or
import com.tek.security.oauth.TekOAuthProperties
import com.tek.security.oauth.exception.TekOAuth2Exception
import com.tek.security.service.AuthService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore
import javax.sql.DataSource

/**OAuth2 Authorization Server Configuration
 *
 * to manage clientId and token validation
 */
@Suppress("UNUSED")
@Configuration
@ConditionalOnBean(OAuthWebSecurity::class)
@EnableAuthorizationServer
class OAuthServer(
    private val datasource: DataSource,
    private val context: ApplicationContext,
    private val authenticationManager: AuthenticationManager,
    private val authService: AuthService,
    private val properties: TekOAuthProperties
) : AuthorizationServerConfigurerAdapter() {

    private val log = LoggerFactory.getLogger(OAuthServer::class.java)

    @Value("\${tek.security.module.type}")
    private lateinit var securityType: TekProperty

    @Bean
    fun tokenStore() = JdbcTokenStore(datasource)

    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.jdbc(datasource)
    }

    override fun configure(security: AuthorizationServerSecurityConfigurer) {

        log.info("Security type: $securityType")

        security
            // unauthenticated access to path: oauth/token with Basic Authentication to get a Bearer Token
            .tokenKeyAccess(isAnonymous().or(hasAuthority(properties.client.authority)))
            // authenticated access to path: oauth/check_token with Basic Authentication (clientId and clientSecret) to get token status
            .checkTokenAccess(hasAuthority(properties.client.authority))
            .passwordEncoder(authService.passwordEncoder())
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {

        endpoints
            .tokenStore(tokenStore())
            .authenticationManager(authenticationManager)
            .userDetailsService(authService)
            .exceptionTranslator(WebResponseExceptionTranslator<OAuth2Exception> {
                if (it is OAuth2Exception) {
                    return@WebResponseExceptionTranslator ResponseEntity<OAuth2Exception>(
                        TekOAuth2Exception(it.message),
                        HttpStatus.BAD_REQUEST
                    )
                } else
                    throw it
            })
    }
}
