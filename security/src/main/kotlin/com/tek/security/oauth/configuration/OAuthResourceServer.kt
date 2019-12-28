package com.tek.security.oauth.configuration

import com.tek.core.SpringProfile
import com.tek.core.i18n.LOCALE_PATTERN
import com.tek.core.util.unreachableCode
import com.tek.security.SecurityPattern.clientResources
import com.tek.security.SecurityPattern.nebularResources
import com.tek.security.SecurityPattern.unauthenticatedPatterns
import com.tek.security.oauth.exception.OAuth2AccessDeniedHandler
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
@ConditionalOnBean(OAuthWebSecurity::class)
@EnableResourceServer
@EnableConfigurationProperties(SecurityProperties::class)
class OAuthResourceServer(
    private val clientDetailsProperties: ClientDetailsProperties,
    private val accessDeniedHandler: OAuth2AccessDeniedHandler,
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

        environment.activeProfiles.map { profile ->
            when (profile) {
                SpringProfile.DEVELOPMENT -> configureDevelopmentSecurity(http)
                SpringProfile.PRODUCTION -> configureProdSecurity(http)
                else -> unreachableCode() // there must always be a profile active (development or production)!
            }
        }
    }

    fun configureDevelopmentSecurity(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers(*unauthenticatedPatterns()).permitAll()
            .antMatchers(LOCALE_PATTERN).permitAll()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/**")
            .access("#oauth2.hasScope('read')")
            .antMatchers(HttpMethod.POST, "/**")
            .access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.PATCH, "/**")
            .access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.PUT, "/**")
            .access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.DELETE, "/**")
            .access("#oauth2.hasScope('write')")
            .anyRequest().authenticated()
            .and()
            .headers().frameOptions().sameOrigin() // allow frame for h2-console
    }

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
            .antMatchers(HttpMethod.PUT, "/**")
            .access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.DELETE, "/**")
            .access("#oauth2.hasScope('write')")
            .anyRequest().authenticated()
    }
}