package com.tek.security.common.service.impl

import com.tek.core.util.isFalse
import com.tek.core.util.orNull
import com.tek.security.common.TekPasswordEncoder
import com.tek.security.common.TekSecurityProperties
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Implementation of [TekAuthService] to get User information and [GrantedAuthority]
 */
@Suppress("unused")
@Service
class TekAuthServiceImpl(
    private val pswEncoder: TekPasswordEncoder,
    private val properties: TekSecurityProperties,
    private val userRepository: TekUserRepository
) : TekAuthService {

    override val passwordRegex =
        Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^*&_])(?=\\S+\$).{8,}\$")

    override fun isValidPassword(password: String) = password.matches(passwordRegex)
    override fun passwordEncoder() = pswEncoder.bcryptEncoder()
    override fun checkPasswordConstraints(
        username: String, email: String?, password: String
    ): Pair<Boolean, String?> {
        if (password == username)
            return Pair(false, username)
        if (password == email)
            return Pair(false, email)
        return Pair(true, null)
    }

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username).orNull()?.apply {
            accountExpired = isAccountExpired(userExpireAt)
            accountLocked = isAccountLocked(lastLogin)
            credentialsExpired = isCredentialsExpired(pwdExpireAt!!)
            accountLocked.isFalse { this.lastLogin = Instant.now() }
        } ?: throw UsernameNotFoundException("User $username not found!")
        return buildTekUserDetails(user)
    }

    override fun getCurrentUser(): TekUserDetails? =
        getAuthentication()?.principal as? TekUserDetails

    override fun getAuthentication(): Authentication? =
        SecurityContextHolder.getContext().authentication

    fun buildTekUserDetails(user: TekUser) = TekUserDetails(
        id = user.id,
        username = user.username!!,
        email = user.email,
        password = user.password!!,
        enabled = user.enabled,
        accountNonExpired = !user.accountExpired,
        credentialsNonExpired = !user.credentialsExpired,
        accountNonLocked = !user.accountLocked,
        authorities = user.profiles.getAuthorities()
    )

    /** Check if User account has expired */
    override fun isAccountExpired(expireAt: LocalDate?): Boolean = expireAt?.let {
        expireAt < LocalDate.now()
    } ?: false

    /** Check if User account has to become locked */
    override fun isAccountLocked(lastLogin: Instant?): Boolean = lastLogin?.let { it ->
        val today = LocalDateTime.now()
        today.minusDays(properties.accountExpiration!!.toDays())
            .isAfter(it.atZone(ZoneId.systemDefault()).toLocalDateTime())
    } ?: false

    /** Check if User password has expired */
    override fun isCredentialsExpired(pswExpireAt: LocalDate): Boolean =
        pswExpireAt < LocalDate.now()

    private fun MutableSet<TekProfile>.getAuthorities(): Set<GrantedAuthority> {
        val roles = mutableSetOf<TekRole>()
        for (profile in this)
            for (role in profile.roles)
                roles.add(role)
        return roles.map { role -> SimpleGrantedAuthority(role.name) }.toSet()
    }
}