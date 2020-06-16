package com.tek.security.common.data

import com.tek.core.TekCoreProperties
import com.tek.core.TekDataRunner
import com.tek.core.util.orNull
import com.tek.security.common.TekPasswordEncoder
import com.tek.security.common.model.*
import com.tek.security.common.repository.TekProfileRepository
import com.tek.security.common.repository.TekRoleRepository
import com.tek.security.common.repository.TekUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
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

    @Autowired
    lateinit var service: DataRunnerService

    enum class ProfileMockup {
        ADMIN,
        AUDIT,
        USER
    }

    override fun runDevelopmentMode() {

        service.performReset()

        val adminRoles = roleRepository.findAll().toMutableSet()
        val adminProfile = createProfile(ProfileMockup.ADMIN, adminRoles)
        val userProfile = createProfile(ProfileMockup.USER, mutableSetOf())

        createUser(
            username = "admin",
            password = "admin",
            email = "admin@gmail.com",
            profiles = mutableSetOf(adminProfile),
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

    private fun createProfile(name: ProfileMockup, roles: MutableSet<TekRole>): TekProfile {
        var profile = profileRepository.findByName(name.name).orNull()
        if (profile == null) {
            profile = profileRepository.save(TekProfile(name.name).apply { this.roles = roles })
        }
        return profile
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

@Service
class DataRunnerService {

    @Autowired
    lateinit var userRepository: TekUserRepository

    @Autowired
    lateinit var profileRepository: TekProfileRepository

    @Autowired
    lateinit var roleRepository: TekRoleRepository

    @Transactional
    fun performReset() {
        userRepository.findAll().forEach { user ->
            userRepository.deleteUserProfilesByUser(user.id!!)
            userRepository.delete(user)
        }
        profileRepository.findAll().forEach { profile ->
            profileRepository.deleteProfileRolesByProfile(profile.id!!)
            profileRepository.delete(profile)
        }
    }
}
