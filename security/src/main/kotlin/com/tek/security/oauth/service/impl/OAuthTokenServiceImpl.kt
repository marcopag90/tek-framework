package com.tek.security.oauth.service.impl

import com.tek.core.util.LoggerDelegate
import com.tek.security.oauth.repository.AccessTokenRepository
import com.tek.security.oauth.service.OAuthTokenService
import com.tek.security.service.AuthService
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore
import org.springframework.stereotype.Service

@Suppress("UNUSED")
@Service
class OAuthTokenServiceImpl(
    private val authService: AuthService,
    private val tokenStore: JdbcTokenStore,
    private val consumerTokenServices: ConsumerTokenServices
) : OAuthTokenService {

    private val log by LoggerDelegate()

    override fun invalidateUserTokens(username: String) {
        log.info("Retrieving user oauth token with username: [$username] ...")
        tokenStore.findTokensByUserName(username)?.forEach { accessToken ->
            log.info("Revoking user access token and refresh token with id: [${accessToken.value}] ...")
            consumerTokenServices.revokeToken(accessToken.value)
        }
    }
}