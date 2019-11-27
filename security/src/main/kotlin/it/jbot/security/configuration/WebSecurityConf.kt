package it.jbot.security.configuration

import it.jbot.security.oauth.configuration.OAuthWebSecurity
import it.jbot.core.SpringProfile
import it.jbot.core.util.SpringProperty
import it.jbot.core.util.unreachableCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@ConditionalOnMissingBean(OAuthWebSecurity::class)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConf(
    private val environment: Environment
) : WebSecurityConfigurerAdapter() {
    
    @Value("\${security.type: basic}")
    private lateinit var securityType: SpringProperty

    private val log: Logger = LoggerFactory.getLogger(WebSecurityConf::class.java)

    override fun configure(http: HttpSecurity) {
        
        log.info("Security type: $securityType")
        
        require(environment.activeProfiles.isNotEmpty()) {
            "At least one Spring profile must be active!"
        }
        
        environment.activeProfiles.find { it == SpringProfile.DEVELOPMENT.label }?.let {
            configureDevelopmentSecurity(http)
        } ?: environment.activeProfiles.find { it == SpringProfile.PRODUCTION.label }?.let {
            configureProdSecurity(http)
        } ?: unreachableCode() // there must always be a profile active (development or production)!
    }
    
    //TODO need to check for client resources antMatchers!
    private fun configureDevelopmentSecurity(http: HttpSecurity) {
        http
            .authorizeRequests()
            .requestMatchers(PathRequest.toH2Console()).permitAll()
            .anyRequest().authenticated()
            .and()
            .headers().frameOptions().sameOrigin()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }
    
    private fun configureProdSecurity(http: HttpSecurity) {
        http
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }
    
}


