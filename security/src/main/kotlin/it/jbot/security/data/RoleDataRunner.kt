package it.jbot.security.data

import it.jbot.security.model.Role
import it.jbot.security.model.enums.RoleName
import it.jbot.security.repository.RoleRepository
import it.jbot.core.util.ifNull
import org.springframework.boot.CommandLineRunner
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
class RoleDataRunner(
    private val roleRepository: RoleRepository
) : CommandLineRunner {
    
    override fun run(vararg args: String?) {
        
        for (rolename in RoleName.values())
            roleRepository.findByName(rolename).ifNull {
                roleRepository.save(Role(rolename))
            }
    }
}