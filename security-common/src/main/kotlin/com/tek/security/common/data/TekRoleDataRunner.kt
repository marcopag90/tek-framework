package com.tek.security.common.data

import com.tek.core.TekCoreProperties
import com.tek.core.data.TekDataRunner
import com.tek.security.common.TekSecurityDataOrder
import com.tek.security.common.model.RoleName
import com.tek.security.common.model.TekRole
import com.tek.security.common.repository.TekRoleRepository
import com.tek.security.common.repository.TekProfileRepository
import com.tek.security.common.repository.TekUserRepository
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Suppress("unused")
@Order(TekSecurityDataOrder.role)
//@Component
class TekRoleDataRunner(
    private val userRepository: TekUserRepository,
    private val profileRepository: TekProfileRepository,
    private val roleRepository: TekRoleRepository,
    coreProperties: TekCoreProperties
) : TekDataRunner<TekRoleDataRunner>(coreProperties, TekRoleDataRunner::class.java) {

    @Transactional
    override fun runDevelopmentMode() {

        userRepository.deleteAll() //cascade delete of users_roles and users
        profileRepository.deleteAll() //cascade delete of roles_privileges and roles
        roleRepository.deleteAll() //cascade delete of privileges

        for (privilegeName in RoleName.values())
            roleRepository.save(TekRole(privilegeName))
    }
}