package it.jbot.security.service

import it.jbot.core.JBotResponse
import it.jbot.core.exception.JBotServiceException
import it.jbot.core.exception.ServiceExceptionData
import it.jbot.core.util.JBotDateUtils
import it.jbot.core.util.ifNot
import it.jbot.security.JBotPasswordEncoder
import it.jbot.security.JBotUserDetails
import it.jbot.security.dto.RegisterForm
import it.jbot.security.i18n.SecurityMessageSource
import it.jbot.security.model.User
import it.jbot.security.model.enums.RoleName
import it.jbot.security.repository.RoleRepository
import it.jbot.security.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/** JBot extension of [UserDetailsService].
 * Use this interface to create custom implementations
 **/
interface AuthService : UserDetailsService {

    fun isValidPassword(password: String): Boolean

    fun passwordEncoder(): PasswordEncoder

    fun getAuthentication(): Authentication?

    fun getCurrentUser(): JBotUserDetails?
}


            

