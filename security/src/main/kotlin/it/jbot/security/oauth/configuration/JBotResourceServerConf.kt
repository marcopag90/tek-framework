package it.jbot.security.oauth.configuration

import it.jbot.security.SecurityConstant.DEFAULT_SECURED_PATTERN
import it.jbot.shared.JBotSpringProfile
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer

/**OAuth2 Resource Server Configuration
 *
 * to manage access to server resources
 */
@Configuration
@ConditionalOnBean(JBotOAuthWebSecurity::class)
@EnableResourceServer
@EnableConfigurationProperties(SecurityProperties::class)
class JBotResourceServerConf(
    private val clientDetailsProperties: ClientDetailsProperties
) : ResourceServerConfigurerAdapter() {
    
    override fun configure(resources: ResourceServerSecurityConfigurer) {
        resources.resourceId(clientDetailsProperties.resourceId)
    }
    
    //TODO prod build configure
    @Profile(JBotSpringProfile.development)
    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .requestMatchers(PathRequest.toH2Console()).permitAll()
            .antMatchers(HttpMethod.GET, DEFAULT_SECURED_PATTERN)
            .access("#oauth2.hasScope('read')")
            .antMatchers(HttpMethod.POST, DEFAULT_SECURED_PATTERN)
            .access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.PATCH, DEFAULT_SECURED_PATTERN)
            .access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.DELETE, DEFAULT_SECURED_PATTERN)
            .access("#oauth2.hasScope('write')")
            .anyRequest().authenticated()
            .and()
            .csrf().disable()
            .headers().frameOptions().sameOrigin()
    }
}