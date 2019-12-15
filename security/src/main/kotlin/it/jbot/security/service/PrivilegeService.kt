package it.jbot.security.service

import it.jbot.core.JBotBaseResponse
import it.jbot.core.service.CrudService
import it.jbot.security.model.Privilege
import org.springframework.http.ResponseEntity

interface PrivilegeService : CrudService<Privilege, Long> {

    fun read(name: String): ResponseEntity<JBotBaseResponse>
}