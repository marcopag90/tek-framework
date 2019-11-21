package it.jbot.security.port

import it.jbot.security.SecurityConstant.REGISTER_PATTERN
import it.jbot.security.dto.RegisterForm
import it.jbot.web.JBotResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid

@RequestMapping("/user")
interface UserPort {

    @PostMapping(REGISTER_PATTERN)
    fun create(@RequestBody @Valid registerForm: RegisterForm): ResponseEntity<JBotResponse>

}