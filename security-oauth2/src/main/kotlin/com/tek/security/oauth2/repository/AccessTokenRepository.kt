package com.tek.security.oauth2.repository

import com.tek.security.oauth2.model.AccessToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccessTokenRepository : JpaRepository<AccessToken, String> {

    fun findByUsername(username: String): AccessToken?
}
