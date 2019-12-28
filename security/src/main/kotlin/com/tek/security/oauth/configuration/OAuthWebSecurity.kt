package com.tek.security.oauth.configuration

import com.tek.security.SecurityPattern
import com.tek.security.service.AuthService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
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
class OAuthWebSecurity(
    private val authService: AuthService
) : WebSecurityConfigurerAdapter() {

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager =
        super.authenticationManagerBean()

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(
            *SecurityPattern.swaggerResources()
        )
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(authService)
            .passwordEncoder(authService.passwordEncoder())
    }
}