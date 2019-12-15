package it.jbot.security.data

import it.jbot.core.util.ifNull
import it.jbot.security.model.Privilege
import it.jbot.security.model.enums.PrivilegeName
import it.jbot.security.repository.PrivilegeRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(DataOrder.privilege)
@Component
class PrivilegeDataRunner(
    private val privilegeRepository: PrivilegeRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        for (privilegeName in PrivilegeName.values())
            privilegeRepository.findByName(privilegeName).ifNull {
                privilegeRepository.save(Privilege(privilegeName))
            }
    }
}