package it.jbot.security.repository

import it.jbot.core.repository.JBotRepository
import it.jbot.security.model.Privilege
import it.jbot.security.model.enums.PrivilegeName
import org.springframework.stereotype.Repository

@Repository
interface PrivilegeRepository : JBotRepository<Privilege, Long> {

    fun findByName(name: PrivilegeName): Privilege?

}