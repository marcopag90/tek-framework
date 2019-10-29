package it.jbot.security.configuration

import it.jbot.security.service.JBotUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class OAuth2WebSecurityConfig(
    private val jBotUserDetailsService: JBotUserDetailsService
    ) : WebSecurityConfigurerAdapter() {
    
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
    
    @Bean
    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()
    
    @Bean
    fun tokenServices(
        jwtTokenStore: JwtTokenStore,
        clientDetailsService: ClientDetailsService
    ): DefaultTokenServices = DefaultTokenServices().apply {
        
        setSupportRefreshToken(true)
        setTokenStore(jwtTokenStore)
        setClientDetailsService(clientDetailsService)
        setAuthenticationManager(authenticationManagerBean())
    }
    
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(jBotUserDetailsService)
            ?.passwordEncoder(passwordEncoder())
    }
}