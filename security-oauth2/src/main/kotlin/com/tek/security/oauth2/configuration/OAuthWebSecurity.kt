package com.tek.security.oauth2.configuration

import com.tek.security.common.service.TekAuthService
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
    prefix = "tek.security.module",
    name = ["type"],
    havingValue = "oauth2"
)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class OAuthWebSecurity(
    private val tekAuthService: TekAuthService
) : WebSecurityConfigurerAdapter() {

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager =
        super.authenticationManagerBean()

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(tekAuthService)
            .passwordEncoder(tekAuthService.passwordEncoder())
    }
}