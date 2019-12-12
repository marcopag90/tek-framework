package it.jbot.security.service

import it.jbot.core.JBotBaseResponse
import it.jbot.core.service.CrudService
import it.jbot.security.model.Role
import org.springframework.http.ResponseEntity

interface RoleService : CrudService<Role, Long> {

    fun read(name: String): ResponseEntity<JBotBaseResponse>
}