package com.tek.security.oauth2.swagger

import com.google.common.base.Predicates
import com.google.common.collect.Lists
import com.tek.security.oauth2.TekOAuthProperties
import com.tek.security.oauth2.configuration.OAuthWebSecurity
import com.tek.security.oauth2.swagger.OAuthSwaggerSecurity.Parameters.SECURITY_SCHEME_NAME
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.OAuthBuilder
import springfox.documentation.service.*
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.swagger.web.SecurityConfiguration
import springfox.documentation.swagger.web.SecurityConfigurationBuilder

@Suppress("UNUSED")
@Configuration
@ConditionalOnBean(OAuthWebSecurity::class)
class OAuthSwaggerSecurity(
    private val properties: TekOAuthProperties
) {

    object Parameters {
        const val SECURITY_SCHEME_NAME = "Spring_OAuth2"
    }

    @Bean
    fun securityInfo(): SecurityConfiguration =
        SecurityConfigurationBuilder.builder()
            .clientId(properties.client.clientId)
            .clientSecret(properties.client.clientSecret)
            .scopeSeparator(" ")
            .useBasicAuthenticationWithAccessCodeGrant(true)
            .build()

    private fun scopes(): Array<AuthorizationScope> = arrayOf(
        AuthorizationScope("read", "for GET methods access"),
        AuthorizationScope("write", "for POST, PATCH, PUT, DELETE methods access")
    )

    private fun securityReferences(): List<SecurityReference> =
        arrayListOf(SecurityReference(SECURITY_SCHEME_NAME, scopes()))

    @Bean
    fun securityScheme(): SecurityScheme {

        val accessTokenHost = properties.accessTokenHost
        val grantType: List<GrantType> = arrayListOf(
            ResourceOwnerPasswordCredentialsGrant("$accessTokenHost/oauth/token")
        )
        return OAuthBuilder()
            .name(SECURITY_SCHEME_NAME)
            .grantTypes(grantType)
            .scopes(Lists.newArrayList(*scopes()))
            .build()
    }

    @Bean
    fun securityContext(): SecurityContext = SecurityContext.builder()
        .securityReferences(securityReferences())
        .forPaths(Predicates.alwaysTrue())
        .build()
}