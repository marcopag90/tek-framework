package com.tek.security.common.data

import com.tek.core.TekCoreProperties
import com.tek.core.data.TekDataRunner
import com.tek.core.util.orNull
import com.tek.security.common.TekSecurityDataOrder
import com.tek.security.common.model.RoleName
import com.tek.security.common.model.TekRole
import com.tek.security.common.model.TekProfile
import com.tek.security.common.repository.TekRoleRepository
import com.tek.security.common.repository.TekProfileRepository
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Suppress("unused")
@Order(TekSecurityDataOrder.profile)
@Component
class TekProfileDataRunner(
    private val tekProfileRepository: TekProfileRepository,
    private val tekRoleRepository: TekRoleRepository,
    coreProperties: TekCoreProperties
) : TekDataRunner<TekProfileDataRunner>(coreProperties, TekProfileDataRunner::class.java) {

    override fun runDevelopmentMode() {
        insertRoles()
    }

    /**
     * Base Profiles definitions for mockup purposes
     */
    internal enum class ProfileName {
        ADMIN,
        AUDIT,
        USER
    }

    private fun insertRoles() {

        for (roleName in ProfileName.values()) {
            when (roleName) {
                ProfileName.ADMIN -> tekProfileRepository.save(ProfileName.ADMIN.createRole(
                    mutableSetOf<TekRole>().apply {
                        this.addAll(tekRoleRepository.findAll().toSet())
                    }
                ))
                ProfileName.AUDIT -> tekProfileRepository.save(ProfileName.AUDIT.createRole(
                    mutableSetOf<TekRole>().apply {
                        tekRoleRepository.findByName(RoleName.AUDIT_READ).orNull()?.let {
                            this.add(it)
                        }
                    }
                ))
                ProfileName.USER -> tekProfileRepository.save(
                    ProfileName.USER.createRole(mutableSetOf())
                )
            }
        }
    }

    private fun ProfileName.createRole(roles: MutableSet<TekRole>): TekProfile =
        TekProfile(this.name).apply { this.roles = roles }
}