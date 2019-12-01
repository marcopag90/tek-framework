package it.jbot.security.port.controller

import it.jbot.core.JBotResponse
import it.jbot.security.dto.RegisterForm
import it.jbot.security.port.AuthPort
import it.jbot.security.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val userService: UserService
) : AuthPort {

    override fun register(registerForm: RegisterForm): ResponseEntity<JBotResponse> {
        return ResponseEntity(
            JBotResponse(userService.register(registerForm)),
            HttpStatus.OK
        )
    }
}