package it.jbot.security.oauth.configuration

import it.jbot.security.JBotPasswordEncoder
import it.jbot.security.oauth.configuration.JwtSecurityProperties.JwtProperties
import it.jbot.security.service.JBotAuthService
import it.jbot.shared.LoggerDelegate
import org.apache.commons.io.IOUtils
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory
import java.nio.charset.StandardCharsets.UTF_8
import java.security.KeyPair
import javax.sql.DataSource

/**OAuth2 Authorization Server Configuration
 *
 * to manage clientId and token validation
 */
@Configuration
@ConditionalOnBean(JBotOAuthWebSecurity::class)
@EnableAuthorizationServer
@EnableConfigurationProperties(JwtSecurityProperties::class)
class JBotAuthServerConf(
    private val datasource: DataSource,
    private val context: ApplicationContext,
    private val jBotPasswordEncoder: JBotPasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jBotAuthService: JBotAuthService,
    private val jwtSecurityProperties: JwtSecurityProperties
) : AuthorizationServerConfigurerAdapter() {
    
    private val logger by LoggerDelegate()
    
    @Bean
    fun oauthAccessDeniedHandler() = OAuth2AccessDeniedHandler()
    
    @Bean
    fun tokenStore() = JdbcTokenStore(datasource)
    
    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        clients?.jdbc(datasource)
    }
    
    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        
        logger.info("Security type: ${context.environment.getProperty("security.type")}")
        
        security
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()")
            .passwordEncoder(jBotPasswordEncoder.encoder())
    }
    
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        
        //TODO access token converter
        endpoints
            .tokenStore(tokenStore())
            .authenticationManager(authenticationManager)
            .userDetailsService(jBotAuthService)
    }
    
    private fun keyPair(
        jwtProperties: JwtProperties,
        keyStoreKeyFactory: KeyStoreKeyFactory
    ): KeyPair {
        return keyStoreKeyFactory.getKeyPair(
            jwtProperties.keyPairAlias,
            jwtProperties.keyPairPassword?.toCharArray()
        )
    }
    
    private fun keyStoreKeyFactory(jwtProperties: JwtProperties): KeyStoreKeyFactory {
        return KeyStoreKeyFactory(
            jwtProperties.keyStore,
            jwtProperties.keyStorePassword?.toCharArray()
        )
    }
    
    private fun getPublicKeyAsString(): String =
        IOUtils.toString(
            jwtSecurityProperties.jwt?.publicKey?.inputStream,
            UTF_8
        )
}

