package it.jbot.security.data

import it.jbot.security.model.Privilege
import it.jbot.security.model.Role
import it.jbot.security.model.enums.PrivilegeName
import it.jbot.security.model.enums.RoleName
import it.jbot.security.repository.PrivilegeRepository
import it.jbot.security.repository.RoleRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

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
                    RoleName.USER -> roleRepository.save(RoleName.USER.createRole(
                        mutableSetOf<Privilege>().apply {
                            privilegeRepository.findByName(PrivilegeName.MENU)?.let {
                                this.add(it)
                            }
                        }
                    ))
                    else -> roleRepository.save(roleName.createRole(mutableSetOf()))
                }
            }
    }

    private fun RoleName.createRole(privileges: MutableSet<Privilege>): Role =
        Role(this).apply {
            this.privileges = privileges
        }
}