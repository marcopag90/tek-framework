package com.tek.security.oauth2.data

import com.tek.core.TekCoreProperties
import com.tek.core.data.TekDataRunner
import com.tek.core.util.doNothing
import com.tek.security.common.SecurityDataOrder
import com.tek.security.common.model.TekPrivilege
import com.tek.security.common.model.TekRole
import com.tek.security.common.model.enums.PrivilegeName
import com.tek.security.common.model.enums.RoleName
import com.tek.security.common.repository.TekPrivilegeRepository
import com.tek.security.common.repository.TekRoleRepository
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Suppress("UNUSED")
@Order(SecurityDataOrder.role)
@Component
class RoleDataRunner(
    private val tekRoleRepository: TekRoleRepository,
    private val tekPrivilegeRepository: TekPrivilegeRepository,
    coreProperties: TekCoreProperties,
    environment: Environment
) : TekDataRunner(environment, coreProperties) {

    override fun runDevelopmentMode() {
        insertRoles()
    }

    override fun runProductionMode() = doNothing()

    private fun insertRoles() {

        for (roleName in RoleName.values()) {
            when (roleName) {
                RoleName.ADMIN -> tekRoleRepository.save(RoleName.ADMIN.createRole(
                    mutableSetOf<TekPrivilege>().apply {
                        this.addAll(tekPrivilegeRepository.findAll().toSet())
                    }
                ))
                RoleName.AUDIT -> tekRoleRepository.save(RoleName.AUDIT.createRole(
                    mutableSetOf<TekPrivilege>().apply {
                        tekPrivilegeRepository.findByName(PrivilegeName.AUDIT_READ)?.let {
                            this.add(it)
                        }
                    }
                ))
                RoleName.USER -> tekRoleRepository.save(RoleName.USER.createRole(
                    mutableSetOf<TekPrivilege>().apply {
                        tekPrivilegeRepository.findByName(PrivilegeName.MENU)?.let {
                            this.add(it)
                        }
                    }
                ))
            }
        }
    }

    private fun RoleName.createRole(tekPrivileges: MutableSet<TekPrivilege>): TekRole =
        TekRole(this).apply {
            this.privileges = tekPrivileges
        }
}