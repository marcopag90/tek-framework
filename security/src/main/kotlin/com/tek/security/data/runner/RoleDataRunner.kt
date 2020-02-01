package com.tek.security.data.runner

import com.tek.core.TekCoreProperties
import com.tek.security.data.DataOrder
import com.tek.security.data.TekSecurityDataRunner
import com.tek.security.model.auth.Privilege
import com.tek.security.model.auth.Role
import com.tek.security.model.enums.PrivilegeName
import com.tek.security.model.enums.RoleName
import com.tek.security.repository.PrivilegeRepository
import com.tek.security.repository.RoleRepository
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Suppress("UNUSED")
@Order(DataOrder.role)
@Component
class RoleDataRunner(
    private val roleRepository: RoleRepository,
    private val privilegeRepository: PrivilegeRepository,
    coreProperties: TekCoreProperties,
    environment: Environment
) : TekSecurityDataRunner(environment, coreProperties) {

    override fun runDevelopmentMode() {
        insertRoles()
    }

    //TODO controllare versione ruoli uno a uno rispetto alla configurazione ed effettuare gli eventuali merge
    override fun runProductionMode() {
        if (roleRepository.count() == 0L)
            insertRoles()
    }

    private fun insertRoles() {

        for (roleName in RoleName.values()) {
            when (roleName) {
                RoleName.ADMIN -> roleRepository.save(RoleName.ADMIN.createRole(
                    mutableSetOf<Privilege>().apply {
                        this.addAll(privilegeRepository.findAll().toSet())
                    }
                ))
                RoleName.AUDIT -> roleRepository.save(RoleName.AUDIT.createRole(
                    mutableSetOf<Privilege>().apply {
                        privilegeRepository.findByName(PrivilegeName.AUDIT_READ)?.let {
                            this.add(it)
                        }
                    }
                ))
                RoleName.USER -> roleRepository.save(RoleName.USER.createRole(
                    mutableSetOf<Privilege>().apply {
                        privilegeRepository.findByName(PrivilegeName.MENU)?.let {
                            this.add(it)
                        }
                    }
                ))
            }
        }
    }

    private fun RoleName.createRole(privileges: MutableSet<Privilege>): Role =
        Role(this).apply {
            this.privileges = privileges
        }
}