package it.jbot.security.port

import it.jbot.core.JBotResponse
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/role")
@PreAuthorize("hasRole('ROLE_ADMIN')")
interface RolePort {

    @GetMapping("/list")
    fun list(pageable: Pageable): ResponseEntity<JBotResponse>

    @GetMapping
    fun read(@RequestParam("name") name: String): ResponseEntity<JBotResponse>

}