package com.tek.security.common.data

import com.tek.core.TekCoreProperties
import com.tek.core.data.TekDataRunner
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
) : TekDataRunner<TekRoleDataRunner>(coreProperties, TekRoleDataRunner::class.java) {

    /**
     * Base Profiles definitions for mockup purposes
     */
    internal enum class ProfileName {
        ADMIN,
        AUDIT,
        USER
    }

    @Transactional
    override fun runDevelopmentMode() {

        RoleName.values().forEach {
            roleRepository.save(TekRole(it))
        }

        val adminProfile = TekProfile().apply {
            name = ProfileName.ADMIN.name

        }

    }

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

        val userRoles = mutableSetOf<TekProfile>()
        for (role in profiles)
            profileRepository.findByName(role.name!!).orNull()?.let {
                userRoles.add(it)
            }

        return TekUser().apply {
            this.username = username
            this.password = pswEncoder.bcryptEncoder().encode(password)
            this.email = email
            this.profiles = userRoles
            this.enabled = enabled
            this.userExpireAt = userExpireAt
            this.pwdExpireAt = pwdExpireAt
            this.lastLogin = lastLogin
        }.let(userRepository::save)
    }

}