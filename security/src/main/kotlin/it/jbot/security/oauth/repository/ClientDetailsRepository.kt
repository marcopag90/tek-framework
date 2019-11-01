package it.jbot.security.oauth.repository

import it.jbot.security.oauth.configuration.JBotOAuthWebSecurity
import it.jbot.security.oauth.model.ClientDetails
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@ConditionalOnBean(JBotOAuthWebSecurity::class)
interface ClientDetailsRepository : JpaRepository<ClientDetails, String> {
}