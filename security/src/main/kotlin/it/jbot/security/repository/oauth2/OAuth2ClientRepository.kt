package it.jbot.security.repository.oauth2

import it.jbot.security.model.oauth2.OAuth2Client
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OAuth2ClientRepository : JpaRepository<OAuth2Client, String> {
}