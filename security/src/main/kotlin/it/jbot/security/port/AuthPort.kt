package it.jbot.security.port

import it.jbot.core.JBotResponse
import it.jbot.security.SecurityConstant
import it.jbot.security.dto.RegisterForm
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid

@RequestMapping("/register")
interface AuthPort {

    @PostMapping(SecurityConstant.REGISTER_PATTERN)
    fun register(@Valid @RequestBody registerForm: RegisterForm): ResponseEntity<JBotResponse>

}