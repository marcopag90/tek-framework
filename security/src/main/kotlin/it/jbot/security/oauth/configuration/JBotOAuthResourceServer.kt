package it.jbot.security.oauth.configuration

import it.jbot.security.SecurityConstant.DEFAULT_SECURED_PATTERN
import it.jbot.security.SecurityConstant.REGISTER_PATTERN
import it.jbot.shared.debug.RequireStatement.SPRING_PROFILE_ACTIVE
import it.jbot.security.oauth.exception.JBotOAuth2AccessDeniedHandler
import it.jbot.shared.SpringProfile
import it.jbot.shared.util.unreachableCode
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
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
class JBotOAuthResourceServer(
    private val clientDetailsProperties: ClientDetailsProperties,
    private val accessDeniedHandler: JBotOAuth2AccessDeniedHandler,
    private val environment: Environment
) : ResourceServerConfigurerAdapter() {
    
    override fun configure(resources: ResourceServerSecurityConfigurer) {
        resources
            .resourceId(clientDetailsProperties.resourceId)
            .accessDeniedHandler(accessDeniedHandler)
    }
    
    override fun configure(http: HttpSecurity) {
    
        require(environment.activeProfiles.isNotEmpty()) {
            SPRING_PROFILE_ACTIVE
        }
        
        environment.activeProfiles.find { it == SpringProfile.development }?.let {
            configureDevelopmentSecurity(http)
        }
            ?: environment.activeProfiles.find { it == SpringProfile.production }?.let {
                configureProdSecurity(http)
            } ?: unreachableCode() // there must always be a profile active (development or production)!
    }
    
    //TODO need to check for client resources antMatchers!
    private fun configureDevelopmentSecurity(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers("/user$REGISTER_PATTERN").permitAll()
            .and()
            .authorizeRequests()
            .requestMatchers(PathRequest.toH2Console())
            .permitAll() // allow h2-console
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
            .cors()
            .disable() // let client app to send request from different port (eg. 4200)
            .headers().frameOptions().sameOrigin() // allow frame for h2-console
    }
    
    private fun configureProdSecurity(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers("/user$REGISTER_PATTERN").permitAll()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, DEFAULT_SECURED_PATTERN)
            .access("#oauth2.hasScope('read')")
            .antMatchers(HttpMethod.POST, DEFAULT_SECURED_PATTERN)
            .access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.PATCH, DEFAULT_SECURED_PATTERN)
            .access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.DELETE, DEFAULT_SECURED_PATTERN)
            .access("#oauth2.hasScope('write')")
            .anyRequest().authenticated()
    }
}