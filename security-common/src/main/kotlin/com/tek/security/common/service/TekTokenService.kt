package com.tek.security.common.service

interface TekTokenService {

    fun invalidateUserTokens(username: String)

}