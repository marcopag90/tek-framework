package com.tek.security.configuration

import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * Configuration lookup for [TekCorsFilter].
 *
 * Default configuration is provided if none is found (development purpose only)
 *
 */
@Configuration
@ConfigurationProperties(prefix = "security.oauth2.cors")
@EnableConfigurationProperties(SecurityProperties::class)
class TekCorsProperties {

    var allowedOrigin: String = "http://localhost:4200"

    var allowedCredentials: String = "true"

    var allowedMethods: Array<String> = arrayOf("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS")

    var allowedHeaders: Array<String> =
        arrayOf("x-requested-with", "authorization", "Content-Type", "Authorization", "credential", "X-XSRF-TOKEN")
}