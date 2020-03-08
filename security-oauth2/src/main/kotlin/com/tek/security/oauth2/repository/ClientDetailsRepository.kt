package com.tek.security.oauth2.repository

import com.tek.security.oauth2.configuration.OAuthWebSecurity
import com.tek.security.oauth2.model.ClientDetails
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@ConditionalOnBean(OAuthWebSecurity::class)
interface ClientDetailsRepository : JpaRepository<ClientDetails, String> {
}