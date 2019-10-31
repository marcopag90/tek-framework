package it.jbot.security.service

import org.springframework.security.core.userdetails.UserDetailsService

/** JBot extension of [UserDetailsService].
 * Use this interface to create custom implementations
 **/
interface JBotAuthService : UserDetailsService {
}