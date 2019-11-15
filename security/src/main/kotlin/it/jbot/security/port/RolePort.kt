package it.jbot.security.port

import it.jbot.shared.web.JBotResponse
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/role")
interface RolePort {

    @GetMapping("/list")
    fun list(pageable: Pageable) : ResponseEntity<JBotResponse>

    @GetMapping
    fun getOne(@RequestParam("name") name: String) : ResponseEntity<JBotResponse>

}