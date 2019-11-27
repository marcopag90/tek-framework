package it.jbot.security.oauth.repository

import it.jbot.security.oauth.configuration.OAuthWebSecurity
import it.jbot.security.oauth.model.ClientDetails
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@ConditionalOnBean(OAuthWebSecurity::class)
interface ClientDetailsRepository : JpaRepository<ClientDetails, String> {
}