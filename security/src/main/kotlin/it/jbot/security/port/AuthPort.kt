package it.jbot.security.port

import it.jbot.security.SecurityConstant
import it.jbot.security.dto.RegisterForm
import it.jbot.web.JBotResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

interface AuthPort {

    @PostMapping(SecurityConstant.REGISTER_PATTERN)
    fun register(@RequestBody registerForm: RegisterForm): ResponseEntity<JBotResponse>

}