package com.tek.security.data

import com.tek.core.util.ifNull
import com.tek.security.model.Privilege
import com.tek.security.model.enums.PrivilegeName
import com.tek.security.repository.PrivilegeRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Suppress("UNUSED")
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