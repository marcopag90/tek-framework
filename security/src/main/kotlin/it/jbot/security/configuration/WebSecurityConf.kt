package it.jbot.security.configuration

import it.jbot.security.oauth.configuration.JBotOAuthWebSecurity
import it.jbot.shared.SpringProfile
import it.jbot.shared.debug.RequireStatement
import it.jbot.shared.util.LoggerDelegate
import it.jbot.shared.util.SpringProperty
import it.jbot.shared.util.unreachableCode
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

@ConditionalOnMissingBean(JBotOAuthWebSecurity::class)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConf(
    private val environment: Environment
) : WebSecurityConfigurerAdapter() {
    
    @Value("\${security.type: basic}")
    private lateinit var securityType: SpringProperty
    
    private val logger by LoggerDelegate()
    
    override fun configure(http: HttpSecurity) {
        
        logger.info("Security type: $securityType")
        
        require(environment.activeProfiles.isNotEmpty()) {
            RequireStatement.SPRING_PROFILE_ACTIVE
        }
        
        environment.activeProfiles.find { it == SpringProfile.development }?.let {
            configureDevelopmentSecurity(http)
        }
            ?: environment.activeProfiles.find { it == SpringProfile.production }?.let {
                configureProdSecurity(http)
            }
            ?: unreachableCode() // there must always be a profile active (development or production)!
    }
    
    //TODO need to check for client resources antMatchers!
    private fun configureDevelopmentSecurity(http: HttpSecurity) {
        http
            .cors().disable()
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


