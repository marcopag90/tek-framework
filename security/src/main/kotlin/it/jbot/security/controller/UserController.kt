package it.jbot.security.controller

import it.jbot.security.dto.RegisterForm
import it.jbot.security.port.UserPort
import it.jbot.security.service.JBotAuthService
import it.jbot.security.service.UserService
import it.jbot.shared.web.JBotResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val authService: JBotAuthService,
    private val userService: UserService
) : UserPort {
    
    override fun register(registerForm: RegisterForm): ResponseEntity<JBotResponse> {
        return ResponseEntity(
            JBotResponse(HttpStatus.OK).apply {
                result = userService.registerUser(registerForm)
            },
            HttpStatus.OK
        )
    }
}