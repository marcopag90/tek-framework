package it.jbot.security.oauth.repository

import it.jbot.security.oauth.model.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, String> {
}