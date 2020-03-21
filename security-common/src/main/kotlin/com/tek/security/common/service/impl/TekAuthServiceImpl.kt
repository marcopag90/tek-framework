package com.tek.security.common.service.impl

import com.tek.core.util.isFalse
import com.tek.core.util.orNull
import com.tek.security.common.TekPasswordEncoder
import com.tek.security.common.TekUserDetails
import com.tek.security.common.model.TekProfile
import com.tek.security.common.model.TekRole
import com.tek.security.common.model.TekUser
import com.tek.security.common.repository.TekUserRepository
import com.tek.security.common.service.TekAuthService
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

/**
 * Implementation of [TekAuthService] to get User information and [GrantedAuthority]
 */
@Suppress("unused")
@Service
class TekAuthServiceImpl(
    private val userRepository: TekUserRepository,
    private val pswEncoder: TekPasswordEncoder
) : TekAuthService {

    protected val passwordRegex =
        Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^*&_])(?=\\S+\$).{8,}\$")

    override fun isValidPassword(password: String) = password.matches(passwordRegex)

    override fun passwordEncoder() = pswEncoder.bcryptEncoder()

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {

        val user = userRepository.findByUsername(username).orNull()?.apply {
            this.accountExpired = isAccountExpired(this.userExpireAt)
            this.accountLocked = isAccountLocked(this.lastLogin)
            this.credentialsExpired = isCredentialsExpired(this.pwdExpireAt!!)
            this.accountLocked.isFalse { this.lastLogin = Instant.now() }
        } ?: throw UsernameNotFoundException("User $username not found!")

        return buildUserDetails(user)
    }

    override fun getCurrentUser(): TekUserDetails? =
        getAuthentication()?.principal as? TekUserDetails

    override fun checkPasswordConstraints(
        username: String,
        email: String?,
        password: String
    ): Pair<Boolean, String?> {
        if (password == username)
            return Pair(false, username)
        if (password == email)
            return Pair(false, email)
        return Pair(true, null)
    }

    override fun getAuthentication(): Authentication? =
        SecurityContextHolder.getContext().authentication

    fun buildUserDetails(user: TekUser) = TekUserDetails(
        id = user.id,
        username = user.username!!,
        email = user.email!!,
        password = user.password!!,
        enabled = user.enabled,
        accountNonExpired = !user.accountExpired,
        credentialsNonExpired = !user.credentialsExpired,
        accountNonLocked = !user.accountLocked,
        authorities = user.profiles.getAuthorities()
    )

    fun MutableSet<TekProfile>.getAuthorities(): Set<GrantedAuthority> {
        val roles = mutableSetOf<TekRole>()
        for (profile in this)
            for (role in profile.roles)
                roles.add(role)
        return roles.map { role -> SimpleGrantedAuthority(role.name.name) }.toSet()
    }
}