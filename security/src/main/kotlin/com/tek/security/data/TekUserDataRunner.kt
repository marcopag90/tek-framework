package com.tek.security.data

import com.tek.core.util.ifNull
import com.tek.security.TekPasswordEncoder
import com.tek.security.model.auth.TekUser
import com.tek.security.model.auth.Role
import com.tek.security.model.enums.RoleName
import com.tek.security.repository.RoleRepository
import com.tek.security.repository.TekUserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.*

@Suppress("UNUSED")
@Order(DataOrder.user)
@Component
class TekUserDataRunner(
    private val userRepository: TekUserRepository,
    private val roleRepository: RoleRepository,
    private val pswEncoder: TekPasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        userRepository.findByUsername("admin").ifNull {
            createUser(
                username = "admin",
                password = "Administrator1!*",
                email = "admin@gmail.com",
                roles = mutableSetOf(
                    Role(name = RoleName.ADMIN),
                    Role(name = RoleName.AUDIT),
                    Role(name = RoleName.USER)
                ),
                enabled = true,
                pwdExpireAt = GregorianCalendar().apply {
                    this.set(2099, GregorianCalendar.DECEMBER, 31)
                }.time
            )
        }

        userRepository.findByUsername("user").ifNull {
            createUser(
                username = "user",
                password = "User1!*",
                email = "user@gmail.com",
                roles = mutableSetOf(
                    Role(name = RoleName.USER)
                ),
                enabled = true,
                pwdExpireAt = GregorianCalendar().apply {
                    this.set(2099, GregorianCalendar.DECEMBER, 31)
                }.time
            )
        }
    }

    private fun createUser(
        username: String,
        password: String,
        email: String,
        roles: MutableSet<Role>,
        enabled: Boolean,
        userExpireAt: Date? = null,
        pwdExpireAt: Date,
        lastLogin: Date? = null
    ): TekUser {

        val userRoles = mutableSetOf<Role>()
        for (role in roles)
            roleRepository.findByName(role.name)?.let {
                userRoles.add(it)
            }

        return TekUser().apply {
            this.username = username
            this.password = pswEncoder.bcryptEncoder().encode(password)
            this.email = email
            this.roles = userRoles
            this.enabled = enabled
            this.userExpireAt = userExpireAt
            this.pwdExpireAt = pwdExpireAt
            this.lastLogin = lastLogin
        }.let(userRepository::save)
    }
}

