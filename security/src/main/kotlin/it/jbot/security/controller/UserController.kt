package it.jbot.security.controller

import it.jbot.security.dto.RegisterForm
import it.jbot.security.service.JBotAuthService
import it.jbot.security.service.UserService
import it.jbot.shared.web.JBotResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/user")
class UserController(
    private val authService: JBotAuthService,
    private val userService: UserService
) {
    
    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody registerForm: RegisterForm): ResponseEntity<JBotResponse> {
        
        return ResponseEntity(
            JBotResponse(HttpStatus.OK).apply {
                result = userService.registerUser(registerForm)
            },
            HttpStatus.OK
        )
    }
}