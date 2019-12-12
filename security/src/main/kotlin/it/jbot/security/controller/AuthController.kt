package it.jbot.security.controller

import it.jbot.core.JBotBaseResponse
import it.jbot.security.SecurityPattern
import it.jbot.security.form.RegisterForm
import it.jbot.security.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(SecurityPattern.REGISTER_PATTERN)
class AuthController(
    private val userService: UserService
) {

    @PostMapping
    fun register(@Valid @RequestBody registerForm: RegisterForm): ResponseEntity<JBotBaseResponse> {
        return ResponseEntity(
            JBotBaseResponse(HttpStatus.OK, userService.register(registerForm)),
            HttpStatus.OK
        )
    }
}