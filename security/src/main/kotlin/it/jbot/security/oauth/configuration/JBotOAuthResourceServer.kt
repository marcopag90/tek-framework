package it.jbot.security.oauth.configuration

import it.jbot.security.SecurityConstant.unauthenticatedPatterns
import it.jbot.security.oauth.exception.JBotOAuth2AccessDeniedHandler
import it.jbot.core.SpringProfile
import it.jbot.core.util.unreachableCode
import it.jbot.security.SecurityConstant.nebularResources
import it.jbot.security.SecurityConstant.clientResources
import it.jbot.web.controller.LOCALE_PATTERN
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

        check(environment.activeProfiles.isNotEmpty()) {
            "At least one Spring profile must be active!"
        }

        environment.activeProfiles.find { it == SpringProfile.DEVELOPMENT.label }?.let {
            configureDevelopmentSecurity(http)
        } ?: environment.activeProfiles.find { it == SpringProfile.PRODUCTION.label }?.let {
            configureProdSecurity(http)
        } ?: unreachableCode()
    }

    fun configureDevelopmentSecurity(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers(*unauthenticatedPatterns()).permitAll()
            .antMatchers(LOCALE_PATTERN).permitAll() // to test locale changes
            .and()
            .authorizeRequests()
            .requestMatchers(PathRequest.toH2Console())
            .permitAll() // allow h2-console
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
            .headers().frameOptions().sameOrigin() // allow frame for h2-console
    }

    //TODO need to check for client resources antMatchers!
    fun configureProdSecurity(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers(*unauthenticatedPatterns()).permitAll()
            .antMatchers(*clientResources()).permitAll()
            .antMatchers(*nebularResources()).permitAll()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/**")
            .access("#oauth2.hasScope('read')")
            .antMatchers(HttpMethod.POST, "/**")
            .access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.PATCH, "/**")
            .access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.DELETE, "/**")
            .access("#oauth2.hasScope('write')")
            .anyRequest().authenticated()
    }
}