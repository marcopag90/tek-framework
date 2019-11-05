package it.jbot.security.service.impl

import it.jbot.security.JBotUserDetails
import it.jbot.security.repository.UserRepository
import it.jbot.security.service.JBotAuthService
import it.jbot.shared.unreachableCode
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Implementation of [JBotAuthService] to get User information and authorities
 */
@Service
class JBotAuthServiceImpl(
    private val userRepository: UserRepository
) : JBotAuthService {
    
    @Transactional
    override fun loadUserByUsername(username: String): UserDetails =
        JBotUserDetails(
            userRepository.findByUserName(username)
                ?: throw UsernameNotFoundException("User $username not found!")
        )
    
    override fun getAuthentication(): Authentication? =
        SecurityContextHolder.getContext().authentication
    
    override fun getCurrentUser(): JBotUserDetails? =
        when (val auth: Authentication? = getAuthentication()) {
            is UsernamePasswordAuthenticationToken -> auth.takeIf { it.principal != null } as JBotUserDetails
            else -> null
        }
}