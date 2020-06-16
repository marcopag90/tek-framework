package com.tek.security.common

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import java.io.Serializable
import java.time.LocalDateTime

/**
 * Class to store User information for Authentication purpose
 *
 * Default values are provided for [User] authentication fields if not implemented in subclasses.
 */
open class TekUserDetails(
    val id: Long?,
    username: String,
    val email: String?,
    password: String,
    enabled: Boolean = true,
    accountNonExpired: Boolean = true,
    credentialsNonExpired: Boolean = true,
    accountNonLocked: Boolean = true,
    authorities: Set<GrantedAuthority> = mutableSetOf()
) : User(
    username,
    password,
    enabled,
    accountNonExpired,
    credentialsNonExpired,
    accountNonLocked,
    authorities
), Serializable





