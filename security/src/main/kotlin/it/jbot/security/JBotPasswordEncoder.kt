package it.jbot.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

/**
 * Configuration for JBot Password Encoders
 */
@Configuration
class JBotPasswordEncoder {

    /**
     * {bcript} password encoder
     */
    @Bean
    fun bcryptEncoder() = BCryptPasswordEncoder()
}
