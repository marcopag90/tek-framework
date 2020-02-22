package com.tek.security.data.runner

import com.tek.core.TekCoreProperties
import com.tek.core.data.TekDataRunner
import com.tek.core.util.doNothing
import com.tek.security.TekPasswordEncoder
import com.tek.security.data.DataOrder
import com.tek.security.model.auth.Role
import com.tek.security.model.auth.TekUser
import com.tek.security.model.enums.RoleName
import com.tek.security.repository.RoleRepository
import com.tek.security.repository.TekUserRepository
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDate

@Suppress("UNUSED")
@Order(DataOrder.user)
@Component
class TekUserDataRunner(
    private val userRepository: TekUserRepository,
    private val roleRepository: RoleRepository,
    private val pswEncoder: TekPasswordEncoder,
    coreProperties: TekCoreProperties,
    environment: Environment
) : TekDataRunner(environment, coreProperties) {

    override fun runDevelopmentMode() {

        createUser(
            username = "admin",
            password = "admin",
            email = "admin@gmail.com",
            roles = roleRepository.findAll().toMutableSet(),
            enabled = true,
            pwdExpireAt = LocalDate.of(2099,12,31)
        )

        createUser(
            username = "user",
            password = "user",
            email = "user@gmail.com",
            roles = mutableSetOf(Role(name = RoleName.USER)),
            enabled = true,
            pwdExpireAt = LocalDate.of(2099,12,31)
        )
    }

    override fun runProductionMode() = doNothing()

    private fun createUser(
        username: String,
        password: String,
        email: String,
        roles: MutableSet<Role>,
        enabled: Boolean,
        userExpireAt: LocalDate? = null,
        pwdExpireAt: LocalDate,
        lastLogin: Instant? = null
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
