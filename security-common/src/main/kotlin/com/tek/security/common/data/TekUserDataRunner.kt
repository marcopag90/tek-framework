package com.tek.security.common.data

import com.tek.core.TekCoreProperties
import com.tek.core.data.TekDataRunner
import com.tek.security.common.TekPasswordEncoder
import com.tek.security.common.TekSecurityDataOrder
import com.tek.security.common.model.TekRole
import com.tek.security.common.model.TekUser
import com.tek.security.common.model.enums.RoleName
import com.tek.security.common.repository.TekRoleRepository
import com.tek.security.common.repository.TekUserRepository
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDate

@Suppress("unused")
@Order(TekSecurityDataOrder.user)
@Component
class TekUserDataRunner(
    private val userRepository: TekUserRepository,
    private val tekRoleRepository: TekRoleRepository,
    private val pswEncoder: TekPasswordEncoder,
    coreProperties: TekCoreProperties
) : TekDataRunner<TekUserDataRunner>(coreProperties, TekUserDataRunner::class.java) {

    override fun runDevelopmentMode() {

        createUser(
            username = "admin",
            password = "admin",
            email = "admin@gmail.com",
            tekRoles = tekRoleRepository.findAll().toMutableSet(),
            enabled = true,
            pwdExpireAt = LocalDate.of(2099, 12, 31)
        )

        createUser(
            username = "user",
            password = "user",
            email = "user@gmail.com",
            tekRoles = mutableSetOf(TekRole(name = RoleName.USER)),
            enabled = true,
            pwdExpireAt = LocalDate.of(2099, 12, 31)
        )
    }

    private fun createUser(
        username: String,
        password: String,
        email: String,
        tekRoles: MutableSet<TekRole>,
        enabled: Boolean,
        userExpireAt: LocalDate? = null,
        pwdExpireAt: LocalDate,
        lastLogin: Instant? = null
    ): TekUser {

        val userRoles = mutableSetOf<TekRole>()
        for (role in tekRoles)
            tekRoleRepository.findByName(role.name)?.let {
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

