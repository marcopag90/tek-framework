package com.tek.security.oauth.data

import com.tek.security.TekPasswordEncoder
import com.tek.security.oauth.configuration.ClientDetailsProperties
import com.tek.security.oauth.configuration.OAuthWebSecurity
import com.tek.security.oauth.model.ClientDetails
import com.tek.security.oauth.repository.ClientDetailsRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Component

@Component
@ConditionalOnBean(OAuthWebSecurity::class)
class ClientDetailsDataRunner(
    private val properties: ClientDetailsProperties,
    private val oAuth2ClientRepository: ClientDetailsRepository,
    private val jBotPasswordEncoder: TekPasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        if (!oAuth2ClientRepository.findById(properties.clientId).isPresent) {

            oAuth2ClientRepository.save(ClientDetails().apply {

                this.clientId = properties.clientId
                this.clientSecret = jBotPasswordEncoder.bcryptEncoder().encode(properties.clientSecret)
                this.resourceId = properties.resourceId
                this.scope = properties.scope
                this.authorizedGrantTypes = properties.grants
                this.authorities = properties.authority
                this.accessTokenValidity = properties.accessTokenValidity.seconds.toInt()
                this.refreshTokenValidity = properties.refreshTokenValidity.seconds.toInt()
            })
        }
    }
}