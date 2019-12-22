package it.jbot.security.service

import it.jbot.core.JBotBaseResponse
import org.springframework.http.ResponseEntity

interface RoleService {

    fun read(name: String): ResponseEntity<JBotBaseResponse>
}