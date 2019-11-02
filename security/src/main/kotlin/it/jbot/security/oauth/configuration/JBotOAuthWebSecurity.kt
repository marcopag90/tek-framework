package it.jbot.security.oauth.configuration

import it.jbot.security.JBotPasswordEncoder
import it.jbot.security.service.JBotAuthService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@ConditionalOnProperty(
    prefix = "security",
    name = ["type"],
    havingValue = "oauth2"
)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class JBotOAuthWebSecurity(
    private val jBotAuthService: JBotAuthService,
    private val passwordEncoder: JBotPasswordEncoder
) : WebSecurityConfigurerAdapter() {
    
    @Bean
    override fun authenticationManagerBean(): AuthenticationManager =
        super.authenticationManagerBean()
    
//    @Bean
//    fun tokenServices(
//        jdbcTokenStore: JdbcTokenStore,
//        clientDetailsService: ClientDetailsService
//    ): DefaultTokenServices = DefaultTokenServices().apply {
//
//        setSupportRefreshToken(true)
//        setTokenStore(jdbcTokenStore)
//        setClientDetailsService(clientDetailsService)
//        setAuthenticationManager(authenticationManagerBean())
//    }
    
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(jBotAuthService)
            .passwordEncoder(passwordEncoder.encoder())
    }
}