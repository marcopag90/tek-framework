package com.tek.security.data.runner

import com.tek.core.TekCoreProperties
import com.tek.core.util.ifNull
import com.tek.security.data.DataOrder
import com.tek.security.data.TekSecurityDataRunner
import com.tek.security.model.auth.Privilege
import com.tek.security.model.enums.PrivilegeName
import com.tek.security.repository.PrivilegeRepository
import com.tek.security.repository.RoleRepository
import com.tek.security.repository.TekUserRepository
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Suppress("UNUSED")
@Order(DataOrder.privilege)
@Component
class PrivilegeDataRunner(
    private val privilegeRepository: PrivilegeRepository,
    private val userRepository: TekUserRepository,
    private val roleRepository: RoleRepository,
    coreProperties: TekCoreProperties,
    environment: Environment
) : TekSecurityDataRunner(environment, coreProperties) {

    override fun runDevelopmentMode() {

        userRepository.deleteAll() //cascade delete of users_roles and users
        roleRepository.deleteAll() //cascade delete of roles_privileges and roles
        privilegeRepository.deleteAll() //cascade delete of privileges

        for (privilegeName in PrivilegeName.values())
            privilegeRepository.save(Privilege(privilegeName))
    }

    override fun runProductionMode() {
        for (privilegeName in PrivilegeName.values())
            privilegeRepository.findByName(privilegeName).ifNull {
                privilegeRepository.save(Privilege(privilegeName))
            }
    }

}