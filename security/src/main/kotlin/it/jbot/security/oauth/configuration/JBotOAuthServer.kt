package it.jbot.security.oauth.configuration

import it.jbot.security.JBotPasswordEncoder
import it.jbot.security.oauth.exception.JBotOAuthException
import it.jbot.security.service.JBotAuthService
import it.jbot.shared.util.concatOR
import it.jbot.shared.util.hasAuthority
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
@ConditionalOnBean(JBotOAuthWebSecurity::class)
@EnableAuthorizationServer
class JBotOAuthServer(
    private val datasource: DataSource,
    private val context: ApplicationContext,
    private val jBotPasswordEncoder: JBotPasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jBotAuthService: JBotAuthService,
    private val clientDetailsProperties: ClientDetailsProperties
) : AuthorizationServerConfigurerAdapter() {

    private val log: Logger = LoggerFactory.getLogger(JBotOAuthServer::class.java)

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
            .tokenKeyAccess("isAnonymous()".concatOR(clientDetailsProperties.authority.hasAuthority()))
            // authenticated access to path: oauth/check_token with Basic Authentication (clientId and clientSecret) to get token status
            .checkTokenAccess(clientDetailsProperties.authority.hasAuthority())
            .passwordEncoder(jBotPasswordEncoder.encoder())
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
                        JBotOAuthException(it.message),
                        HttpStatus.BAD_REQUEST
                    )
                } else
                    throw it
            })
    }
}
