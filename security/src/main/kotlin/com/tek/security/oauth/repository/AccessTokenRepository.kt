package com.tek.security.oauth.repository

import com.tek.security.oauth.model.AccessToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccessTokenRepository : JpaRepository<AccessToken, String> {
}