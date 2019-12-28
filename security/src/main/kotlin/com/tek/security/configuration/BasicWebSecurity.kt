package com.tek.security.configuration

import com.tek.core.SpringProfile
import com.tek.core.util.LoggerDelegate
import com.tek.core.util.SpringProperty
import com.tek.core.util.unreachableCode
import com.tek.security.SecurityPattern
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
@ConditionalOnProperty(
    prefix = "security",
    name = ["type"],
    havingValue = "basic"
)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class BasicWebSecurity(
    private val environment: Environment
) : WebSecurityConfigurerAdapter() {

    @Value("\${security.type: basic}")
    private lateinit var securityType: SpringProperty

    private val log by LoggerDelegate()

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(
            *SecurityPattern.swaggerResources()
        )
    }

    override fun configure(http: HttpSecurity) {

        log.info("Security type: $securityType")

        require(environment.activeProfiles.isNotEmpty()) {
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

    //TODO need to check for client resources antMatchers!
    private fun configureDevelopmentSecurity(http: HttpSecurity) {
        http
            .authorizeRequests()
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

