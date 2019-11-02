package it.jbot.security.oauth.configuration

import it.jbot.security.SecurityConstant.DEFAULT_SECURED_PATTERN
import it.jbot.security.oauth.OAuthConstant.DEFAULT_RESOURCE_ID
import it.jbot.security.oauth.OAuthConstant.SECURED_READER_SCOPE
import it.jbot.security.oauth.OAuthConstant.SECURED_WRITER_SCOPE
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
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
class JBotResourceServerConf : ResourceServerConfigurerAdapter() {
    
    override fun configure(resources: ResourceServerSecurityConfigurer) {
        resources.resourceId(DEFAULT_RESOURCE_ID)
    }
    
    //TODO prod build configure
    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .requestMatchers(PathRequest.toH2Console()).permitAll()
            .antMatchers(HttpMethod.GET, DEFAULT_SECURED_PATTERN)
            .access(SECURED_READER_SCOPE)
            .antMatchers(HttpMethod.POST, DEFAULT_SECURED_PATTERN)
            .access(SECURED_WRITER_SCOPE)
            .antMatchers(HttpMethod.PATCH, DEFAULT_SECURED_PATTERN)
            .access(SECURED_WRITER_SCOPE)
            .antMatchers(HttpMethod.DELETE, DEFAULT_SECURED_PATTERN)
            .access(SECURED_WRITER_SCOPE)
            .anyRequest().authenticated()
            .and()
            .csrf().disable()
            .headers().frameOptions().sameOrigin()
    }
}