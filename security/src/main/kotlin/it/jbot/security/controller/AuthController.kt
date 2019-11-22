package it.jbot.security.controller

import it.jbot.security.dto.RegisterForm
import it.jbot.security.port.AuthPort
import it.jbot.security.service.UserService
import it.jbot.web.JBotResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val userService: UserService
) : AuthPort {

    override fun register(registerForm: RegisterForm): ResponseEntity<JBotResponse> {

        return ResponseEntity(
            JBotResponse(HttpStatus.OK).apply {
                this.result = userService.registerUser(registerForm)
            },
            HttpStatus.OK
        )
    }
}