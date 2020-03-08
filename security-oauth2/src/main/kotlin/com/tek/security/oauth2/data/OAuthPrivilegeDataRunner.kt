package com.tek.security.oauth2.data

import com.tek.core.TekCoreProperties
import com.tek.core.data.TekDataRunner
import com.tek.security.common.TekSecurityDataOrder
import com.tek.security.common.model.TekPrivilege
import com.tek.security.common.model.enums.PrivilegeName
import com.tek.security.common.repository.TekPrivilegeRepository
import com.tek.security.common.repository.TekRoleRepository
import com.tek.security.common.repository.TekUserRepository
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Suppress("unused")
@Order(TekSecurityDataOrder.privilege)
@Component
class OAuthPrivilegeDataRunner(
    private val tekPrivilegeRepository: TekPrivilegeRepository,
    private val userRepository: TekUserRepository,
    private val tekRoleRepository: TekRoleRepository,
    coreProperties: TekCoreProperties
) : TekDataRunner<OAuthPrivilegeDataRunner>(coreProperties, OAuthPrivilegeDataRunner::class.java) {

    override fun runDevelopmentMode() {

        userRepository.deleteAll() //cascade delete of users_roles and users
        tekRoleRepository.deleteAll() //cascade delete of roles_privileges and roles
        tekPrivilegeRepository.deleteAll() //cascade delete of privileges

        for (privilegeName in PrivilegeName.values())
            tekPrivilegeRepository.save(TekPrivilege(privilegeName))
    }
}