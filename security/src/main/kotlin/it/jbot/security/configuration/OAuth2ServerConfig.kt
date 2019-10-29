package it.jbot.security.configuration

import it.jbot.security.configuration.JwtSecurityProperties.JwtProperties
import it.jbot.security.service.JBotUserDetailsService
import org.apache.commons.io.IOUtils
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory
import java.nio.charset.StandardCharsets.UTF_8
import java.security.KeyPair
import javax.sql.DataSource

//TODO see misunderstanding about having two split token service/token store in res and auth server..maybe it's better to provide a single bean in WebSecurityConfigurerAdapter
/**OAuth2 Authorization Server Configuration
 *
 * to manage clientId and token validation
 */
@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(JwtSecurityProperties::class)
class JBotAuthServerConfig(
    private val datasource: DataSource,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtSecurityProperties: JwtSecurityProperties,
    private val jBotUserDetailsService: JBotUserDetailsService
) : AuthorizationServerConfigurerAdapter() {
    
    @Bean
    fun tokenStore(): JwtTokenStore = JwtTokenStore(jwtAccessTokenConverter())
    
    @Bean
    fun jwtAccessTokenConverter(): JwtAccessTokenConverter {
        return JwtAccessTokenConverter().apply {
            setKeyPair(
                keyPair(
                    jwtProperties = jwtSecurityProperties.jwt!!,
                    keyStoreKeyFactory = keyStoreKeyFactory(jwtSecurityProperties.jwt!!)
                )
            )
            setVerifierKey(getPublicKeyAsString())
        }
    }
    
    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        clients?.jdbc(datasource)
    }
    
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
        endpoints?.authenticationManager(authenticationManager)
            ?.accessTokenConverter(jwtAccessTokenConverter())
            ?.userDetailsService(jBotUserDetailsService)
            ?.tokenStore(tokenStore())
    }
    
    override fun configure(security: AuthorizationServerSecurityConfigurer?) {
        security?.passwordEncoder(passwordEncoder)
            ?.tokenKeyAccess("permitAll()")
            ?.checkTokenAccess("isAuthenticated()")
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
        return KeyStoreKeyFactory(jwtProperties.keyStore, jwtProperties.keyStorePassword?.toCharArray())
    }
    
    private fun getPublicKeyAsString(): String =
        IOUtils.toString(jwtSecurityProperties.jwt?.publicKey?.inputStream, UTF_8)
}

/**OAuth2 Resource Server Configuration
 *
 * to manage access to server resources
 */
@Configuration
@EnableResourceServer
@EnableConfigurationProperties(SecurityProperties::class)
class JBotResourceServerConfig : ResourceServerConfigurerAdapter() {
    
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .requestMatchers(PathRequest.toH2Console()).permitAll()
            .antMatchers(HttpMethod.GET, "/**").access("#oauth2.hasScope('read')")
            .antMatchers(HttpMethod.POST, "/**").access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.PATCH, "/**").access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.DELETE, "/**").access("#oauth2.hasScope('write')")
            .anyRequest().authenticated()
            .and()
            .headers().frameOptions().sameOrigin()
            .and()
            .csrf().disable()
    }
}