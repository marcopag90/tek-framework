package com.tek.security.oauth2.data

import com.tek.security.common.TekPasswordEncoder
import com.tek.security.oauth2.TekOAuthProperties
import com.tek.security.oauth2.configuration.OAuthWebSecurity
import com.tek.security.oauth2.model.ClientDetails
import com.tek.security.oauth2.repository.ClientDetailsRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Component

@Suppress("unused")
@Component
@ConditionalOnBean(OAuthWebSecurity::class)
class ClientDetailsDataRunner(
    private val oauthProps: TekOAuthProperties,
    private val oAuth2ClientRepository: ClientDetailsRepository,
    private val pswEncoder: TekPasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        if (!oAuth2ClientRepository.findById(oauthProps.client.clientId).isPresent) {

            oAuth2ClientRepository.save(ClientDetails().apply {

                this.clientId = oauthProps.client.clientId
                this.clientSecret =
                    pswEncoder.bcryptEncoder().encode(oauthProps.client.clientSecret)
                this.resourceId = oauthProps.client.resourceId
                this.scope = oauthProps.client.scope
                this.authorizedGrantTypes = oauthProps.client.grants
                this.authorities = oauthProps.client.authority
                this.accessTokenValidity = oauthProps.client.accessTokenValidity?.seconds?.toInt()
                this.refreshTokenValidity = oauthProps.client.refreshTokenValidity?.seconds?.toInt()
            })
        }
    }
}