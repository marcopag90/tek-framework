package it.jbot.security.configuration

import it.jbot.security.oauth.configuration.JBotOAuthWebSecurity
import it.jbot.shared.JBotSpringProfile.development
import it.jbot.shared.LoggerDelegate
import it.jbot.shared.SpringProperty
import org.bouncycastle.asn1.iana.IANAObjectIdentifiers.security
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Profile(development)
@ConditionalOnMissingBean(JBotOAuthWebSecurity::class)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConf : WebSecurityConfigurerAdapter() {
    
    @Value("\${security.type: basic}")
    private lateinit var securityType: SpringProperty
    
    private val logger by LoggerDelegate()
    
    override fun configure(http: HttpSecurity) {
    
        logger.info("Security type: $securityType")
        
        http
            .csrf().disable()
            .authorizeRequests()
            .requestMatchers(PathRequest.toH2Console()).permitAll()
            .anyRequest().authenticated()
            .and()
            .headers().frameOptions().sameOrigin()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }
    
}


