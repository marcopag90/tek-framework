package it.jbot.security.service.impl

import it.jbot.security.JBotUserDetails
import it.jbot.security.repository.UserRepository
import it.jbot.security.service.JBotAuthService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Implementation of [JBotAuthService] to get User information and authorities
 */
@Service
class JBotAuthServiceImpl(
    private val userRepository: UserRepository
) : JBotAuthService {
    
    override fun loadUserByUsername(username: String): UserDetails =
        JBotUserDetails(
            userRepository.findByUserName(username)
                ?: throw UsernameNotFoundException("User $username not found!")
        )
}