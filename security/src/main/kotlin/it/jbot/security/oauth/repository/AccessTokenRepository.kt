package it.jbot.security.oauth.repository

import it.jbot.security.oauth.model.AccessToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccessTokenRepository : JpaRepository<AccessToken, String> {
}