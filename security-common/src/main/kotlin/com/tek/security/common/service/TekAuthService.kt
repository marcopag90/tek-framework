package com.tek.security.common.service

import com.tek.security.common.TekUserDetails
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import java.time.LocalDate
import kotlin.properties.Delegates

/** Tek extension of [UserDetailsService].
 * Use this interface to create custom implementations.
 **/
interface TekAuthService : UserDetailsService {

    val passwordRegex: Regex
    fun isValidPassword(password: String): Boolean
    fun passwordEncoder(): PasswordEncoder
    fun checkPasswordConstraints(
        username: String, email: String?, password: String
    ): Pair<Boolean, String?>

    fun getAuthentication(): Authentication?
    fun getCurrentUser(): TekUserDetails?

    fun isAccountExpired(expireAt: LocalDate?): Boolean
    fun isAccountLocked(lastLogin: Instant?): Boolean
    fun isCredentialsExpired(pswExpireAt: LocalDate): Boolean
}


            

