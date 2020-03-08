package com.tek.security.oauth2.data

import com.tek.security.common.TekPasswordEncoder
import com.tek.security.oauth2.TekOAuthProperties
import com.tek.security.oauth2.model.ClientDetails
import com.tek.security.oauth2.repository.ClientDetailsRepository
import com.tek.security.oauth2.configuration.OAuthWebSecurity
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Component

@Suppress("unused")
@Component
@ConditionalOnBean(OAuthWebSecurity::class)
class ClientDetailsDataRunner(
    private val properties: TekOAuthProperties,
    private val oAuth2ClientRepository: ClientDetailsRepository,
    private val pswEncoder: TekPasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        if (!oAuth2ClientRepository.findById(properties.client.clientId).isPresent) {

            oAuth2ClientRepository.save(ClientDetails().apply {

                this.clientId = properties.client.clientId
                this.clientSecret =
                    pswEncoder.bcryptEncoder().encode(properties.client.clientSecret)
                this.resourceId = properties.client.resourceId
                this.scope = properties.client.scope
                this.authorizedGrantTypes = properties.client.grants
                this.authorities = properties.client.authority
                this.accessTokenValidity = properties.client.accessTokenValidity?.seconds?.toInt()
                this.refreshTokenValidity = properties.client.refreshTokenValidity?.seconds?.toInt()
            })
        }
    }
}