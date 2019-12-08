package it.jbot.security.service.impl

import it.jbot.security.JBotPasswordEncoder
import it.jbot.security.JBotUserDetails
import it.jbot.security.repository.UserRepository
import it.jbot.security.service.AuthService
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Implementation of [AuthService] to get User information and authorities
 */
@Service
class AuthServiceImpl(
    private val userRepository: UserRepository
) : AuthService {

    private val passwordRegex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^*&_])(?=\\S+\$).{8,}\$")

    override fun isValidPassword(password: String) = password.matches(passwordRegex)

    override fun passwordEncoder() = JBotPasswordEncoder().encoder()

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {

        // retrieve user from repository and transactionally update User fields if needed
        var user = userRepository.findByUserName(username)?.apply {

            this.lastLogin = Date()

            this.accountExpired = isAccountExpired(this.userExpireAt)
            this.accountLocked = isAccountLocked(this.lastLogin)
            this.credentialsExpired = isCredentialsExpired(this.pwdExpireAt!!)

        } ?: throw UsernameNotFoundException("User $username not found!")

        // evaluate UserDetails for Authentication
        return JBotUserDetails(user).apply {

            this.accountExpired = user.accountExpired
            this.accountLocked = user.accountLocked
            this.credentialsExpired = user.credentialsExpired
        }
    }

    override fun getAuthentication(): Authentication? =
        SecurityContextHolder.getContext().authentication

    override fun getCurrentUser(): JBotUserDetails? =
        when (val auth: Authentication? = getAuthentication()) {
            //TODO check for basic auth implementation
            is OAuth2Authentication -> auth.principal as JBotUserDetails?
            else -> null
        }
}