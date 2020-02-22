package com.tek.security.oauth.service

interface OAuthTokenService {

    fun invalidateUserTokens(username: String)

}