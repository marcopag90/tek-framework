package it.jbot.security.controller

import it.jbot.security.dto.RegisterForm
import it.jbot.security.port.UserPort
import it.jbot.security.service.UserService
import it.jbot.web.JBotResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

//@RestController
class UserController(
    private val userService: UserService
) : UserPort {

}