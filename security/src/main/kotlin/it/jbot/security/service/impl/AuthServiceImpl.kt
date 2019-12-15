package it.jbot.security.service.impl

import it.jbot.core.util.isFalse
import it.jbot.security.JBotPasswordEncoder
import it.jbot.security.JBotUserDetails
import it.jbot.security.model.JBotUser
import it.jbot.security.model.Privilege
import it.jbot.security.model.Role
import it.jbot.security.repository.UserRepository
import it.jbot.security.service.AuthService
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Implementation of [AuthService] to get User information and [GrantedAuthority]
 */
@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val jBotPasswordEncoder: JBotPasswordEncoder
) : AuthService {

    private val passwordRegex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^*&_])(?=\\S+\$).{8,}\$")

    override fun isValidPassword(password: String) = password.matches(passwordRegex)

    override fun passwordEncoder() = jBotPasswordEncoder.bcryptEncoder()

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {

        val user = userRepository.findByUsername(username)?.apply {
            this.accountExpired = isAccountExpired(this.userExpireAt)
            this.accountLocked = isAccountLocked(this.lastLogin)
            this.credentialsExpired = isCredentialsExpired(this.pwdExpireAt!!)
            this.accountLocked.isFalse { this.lastLogin = Date() }
        } ?: throw UsernameNotFoundException("User $username not found!")

        return buildUserDetails(user)
    }

    override fun getCurrentUser(): JBotUserDetails? =
        when (val auth: Authentication? = getAuthentication()) {
            //TODO check for basic auth implementation
            is OAuth2Authentication -> auth.principal as JBotUserDetails?
            else -> null
        }

    override fun checkPasswordConstraints(username: String, email: String, password: String): Boolean =
        password != username && password != email

    override fun getAuthentication(): Authentication? =
        SecurityContextHolder.getContext().authentication

    fun buildUserDetails(user: JBotUser) = JBotUserDetails(
        id = user.id,
        username = user.username,
        email = user.email,
        password = user.password,
        enabled = user.enabled,
        accountNonExpired = !user.accountExpired,
        credentialsNonExpired = !user.credentialsExpired,
        accountNonLocked = !user.accountLocked,
        authorities = user.roles.getAuthorities()
    )

    fun MutableSet<Role>.getAuthorities(): Set<GrantedAuthority> {

        val privileges = mutableSetOf<Privilege>()
        for (role in this)
            for (privilege in role.privileges)
                privileges.add(privilege)

        return privileges.map { privilege -> SimpleGrantedAuthority(privilege.name.name) }.toSet()
    }
}