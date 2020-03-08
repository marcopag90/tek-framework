package com.tek.security.common

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 * Configuration for JBot Password Encoders
 */
@Configuration
class TekPasswordEncoder {

    /**
     * {bcript} password encoder
     */
    @Bean
    fun bcryptEncoder() = BCryptPasswordEncoder()
}
