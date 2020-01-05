package com.tek.security.data

import com.tek.security.model.auth.Privilege
import com.tek.security.model.auth.Role
import com.tek.security.model.enums.PrivilegeName
import com.tek.security.model.enums.RoleName
import com.tek.security.repository.PrivilegeRepository
import com.tek.security.repository.RoleRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Suppress("UNUSED")
@Order(DataOrder.role)
@Component
class RoleDataRunner(
    private val roleRepository: RoleRepository,
    private val privilegeRepository: PrivilegeRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        if (roleRepository.count() == 0L)
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