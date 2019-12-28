package com.tek.security.service

import com.tek.security.TekUserDetails
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

/** JBot extension of [UserDetailsService].
 * Use this interface to create custom implementations
 **/
interface AuthService : UserDetailsService {

    fun isValidPassword(password: String): Boolean

    fun passwordEncoder(): PasswordEncoder

    fun getAuthentication(): Authentication?

    fun getCurrentUser(): TekUserDetails?

    fun checkPasswordConstraints(username: String, email: String, password: String): Boolean
}


            

