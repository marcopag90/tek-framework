package it.jbot.security.oauth.configuration

import it.jbot.security.oauth.exception.JBotOAuth2Exception
import it.jbot.security.service.AuthService
import it.jbot.core.util.or
import it.jbot.core.util.hasAuthority
import it.jbot.core.util.isAnonymous
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
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
    private val context: ApplicationContext,
    private val authenticationManager: AuthenticationManager,
    private val jBotAuthService: AuthService,
    private val clientDetailsProperties: ClientDetailsProperties
) : AuthorizationServerConfigurerAdapter() {

    private val log: Logger = LoggerFactory.getLogger(OAuthServer::class.java)

    @Bean
    fun tokenStore() = JdbcTokenStore(datasource)

    @Bean
    fun corsFilter(): List<CorsFilter> =
        listOf(CorsFilter(UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/oauth/token", CorsConfiguration().applyPermitDefaultValues())
        }))

    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.jdbc(datasource)
    }

    override fun configure(security: AuthorizationServerSecurityConfigurer) {

        log.info("Security type: ${context.environment.getProperty("security.type")}")

        security
            // unauthenticated access to path: oauth/token with Basic Authentication to get a Bearer Token
            .tokenKeyAccess(
                isAnonymous().or(
                    hasAuthority(
                        clientDetailsProperties.authority
                    )
                ))
            // authenticated access to path: oauth/check_token with Basic Authentication (clientId and clientSecret) to get token status
            .checkTokenAccess(hasAuthority(clientDetailsProperties.authority))
            .passwordEncoder(jBotAuthService.passwordEncoder())
            .tokenEndpointAuthenticationFilters(corsFilter())
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {

        endpoints
            .tokenStore(tokenStore())
            .authenticationManager(authenticationManager)
            .userDetailsService(jBotAuthService)
            .exceptionTranslator(WebResponseExceptionTranslator<OAuth2Exception> {
                if (it is OAuth2Exception) {
                    return@WebResponseExceptionTranslator ResponseEntity<OAuth2Exception>(
                        JBotOAuth2Exception(it.message),
                        HttpStatus.BAD_REQUEST
                    )
                } else
                    throw it
            })
    }
}