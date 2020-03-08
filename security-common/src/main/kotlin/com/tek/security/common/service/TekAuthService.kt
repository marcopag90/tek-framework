package com.tek.security.common.service

import com.tek.security.common.TekUserDetails
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

/** Tek extension of [UserDetailsService].
 * Use this interface to create custom implementations
 **/
interface TekAuthService : UserDetailsService {

    fun isValidPassword(password: String): Boolean

    fun passwordEncoder(): PasswordEncoder

    fun getAuthentication(): Authentication?

    fun getCurrentUser(): TekUserDetails?

    fun checkPasswordConstraints(username: String, email: String, password: String): Pair<Boolean, String?>
}


            

