package it.jbot.security.repository

import it.jbot.core.repository.JBotRepository
import it.jbot.security.model.Role
import it.jbot.security.model.enums.RoleName
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JBotRepository<Role, Long> {

    fun findByName(name: RoleName): Role?
}