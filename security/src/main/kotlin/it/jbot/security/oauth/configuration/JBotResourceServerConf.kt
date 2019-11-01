package it.jbot.security.oauth.configuration

import org.apache.commons.io.IOUtils
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import java.nio.charset.StandardCharsets

/**OAuth2 Resource Server Configuration
 *
 * to manage access to server resources
 */
@Configuration
@ConditionalOnBean(JBotOAuthWebSecurity::class)
@EnableResourceServer
@EnableConfigurationProperties(SecurityProperties::class)
class JBotResourceServerConf(
    private val jwtSecurityProperties: JwtSecurityProperties
) : ResourceServerConfigurerAdapter() {
    
    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .requestMatchers(PathRequest.toH2Console()).permitAll()
            .antMatchers(HttpMethod.GET, "/**")
            .access("#oauth2.hasScope('read')")
            .antMatchers(HttpMethod.POST, "/**")
            .access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.PATCH, "/**")
            .access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.DELETE, "/**")
            .access("#oauth2.hasScope('write')")
            .anyRequest().authenticated()
            .and()
            .csrf().disable()
            .headers().frameOptions().sameOrigin()
    }
    
    @Bean
    fun jwtResourceAccessTokenConverter(): JwtAccessTokenConverter {
        return JwtAccessTokenConverter().apply {
            setVerifierKey(getPublicKeyAsString())
        }
    }
    
    private fun getPublicKeyAsString(): String =
        IOUtils.toString(
            jwtSecurityProperties.jwt?.publicKey?.inputStream,
            StandardCharsets.UTF_8
        )
}