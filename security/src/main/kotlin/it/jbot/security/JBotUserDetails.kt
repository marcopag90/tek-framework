package it.jbot.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import java.io.Serializable

/**
 * POJO to store User information for Authentication purpose
 */
open class JBotUserDetails(
    val id: Long?,
    username: String,
    val email: String,
    password: String,
    enabled: Boolean,
    accountNonExpired: Boolean,
    credentialsNonExpired: Boolean,
    accountNonLocked: Boolean,
    authorities: Set<GrantedAuthority>
) : User(
    username,
    password,
    enabled,
    accountNonExpired,
    credentialsNonExpired,
    accountNonLocked,
    authorities
), Serializable


