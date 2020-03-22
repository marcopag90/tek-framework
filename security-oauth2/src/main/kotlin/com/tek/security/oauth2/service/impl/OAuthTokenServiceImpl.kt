package com.tek.security.oauth2.service.impl

import com.tek.core.util.LoggerDelegate
import com.tek.security.common.service.TekAuthService
import com.tek.security.common.service.TekTokenService
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Suppress("unused")
@Service
class OAuthTokenServiceImpl(
    private val tekAuthService: TekAuthService,
    private val tokenStore: JdbcTokenStore,
    private val consumerTokenServices: ConsumerTokenServices
) : TekTokenService {

    private val log by LoggerDelegate()

    @Transactional
    override fun invalidateUserTokens(username: String) {
        log.info("Retrieving user oauth token with username: [$username] ...")
        tokenStore.findTokensByUserName(username)?.forEach { accessToken ->
            log.info("Revoking user access token and refresh token with id: [${accessToken.value}] ...")
            consumerTokenServices.revokeToken(accessToken.value)
        }
    }
}