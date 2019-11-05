package it.jbot.security.service

import it.jbot.security.JBotUserDetails
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService

/** JBot extension of [UserDetailsService].
 * Use this interface to create custom implementations
 **/
interface JBotAuthService : UserDetailsService {
    
    fun getAuthentication() : Authentication?
    
    fun getCurrentUser(): JBotUserDetails?
}