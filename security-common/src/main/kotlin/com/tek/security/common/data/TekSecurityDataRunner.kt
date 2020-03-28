package com.tek.security.common.data

import com.tek.core.TekCoreProperties
import com.tek.core.TekDataRunner
import com.tek.core.util.orNull
import com.tek.security.common.TekPasswordEncoder
import com.tek.security.common.model.RoleName
import com.tek.security.common.model.TekProfile
import com.tek.security.common.model.TekRole
import com.tek.security.common.model.TekUser
import com.tek.security.common.repository.TekProfileRepository
import com.tek.security.common.repository.TekRoleRepository
import com.tek.security.common.repository.TekUserRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDate

@Suppress("unused")
@Component
class TekSecurityDataRunner(
    private val userRepository: TekUserRepository,
    private val profileRepository: TekProfileRepository,
    private val roleRepository: TekRoleRepository,
    private val pswEncoder: TekPasswordEncoder,
    coreProperties: TekCoreProperties
) : TekDataRunner<TekSecurityDataRunner>(coreProperties, TekSecurityDataRunner::class.java) {

    /**
     * Base Profiles definitions for mockup purposes
     */
    enum class ProfileMockup {
        ADMIN,
        AUDIT,
        USER
    }

    @Transactional
    override fun runDevelopmentMode() {

        val roles = mutableSetOf<TekRole>()
        RoleName.values().forEach { rolename -> createRole(rolename).let(roles::add) }

        val auditRoles = roles.filter { it.name == RoleName.AUDIT_READ }.toMutableSet()

        val adminProfile = createProfile(ProfileMockup.ADMIN, roles)
        val auditProfile = createProfile(ProfileMockup.AUDIT, auditRoles)
        val userProfile = createProfile(ProfileMockup.USER, mutableSetOf())

        createUser(
            username = "admin",
            password = "admin",
            email = "admin@gmail.com",
            profiles = mutableSetOf(adminProfile, auditProfile),
            enabled = true,
            pwdExpireAt = LocalDate.of(2099, 12, 31)
        )

        createUser(
            username = "user",
            password = "user",
            email = "user@gmail.com",
            profiles = mutableSetOf(userProfile),
            enabled = true,
            pwdExpireAt = LocalDate.of(2099, 12, 31)
        )
    }

    @Transactional
    private fun createRole(roleName: RoleName): TekRole {
        var role = roleRepository.findByName(roleName).orNull()
        if (role == null) {
            role = roleRepository.save(TekRole(roleName))
        }
        return role
    }

    @Transactional
    private fun createProfile(name: ProfileMockup, roles: MutableSet<TekRole>): TekProfile {
        var profile = profileRepository.findByName(name.name).orNull()
        if (profile == null) {
            profile = profileRepository.save(TekProfile(name.name).apply { this.roles = roles })
        }
        return profile
    }

    @Transactional
    private fun createUser(
        username: String,
        password: String,
        email: String,
        profiles: MutableSet<TekProfile>,
        enabled: Boolean,
        userExpireAt: LocalDate? = null,
        pwdExpireAt: LocalDate,
        lastLogin: Instant? = null
    ): TekUser {
        var user = userRepository.findByUsername(username).orNull()
        if (user == null) {
            user = userRepository.save(TekUser().apply {
                this.username = username
                this.password = pswEncoder.bcryptEncoder().encode(password)
                this.email = email
                this.profiles = profiles
                this.enabled = enabled
                this.userExpireAt = userExpireAt
                this.pwdExpireAt = pwdExpireAt
                this.lastLogin = lastLogin
            })
        }
        return user
    }
}