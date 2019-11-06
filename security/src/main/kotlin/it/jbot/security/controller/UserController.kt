package it.jbot.security.controller

import it.jbot.security.service.JBotAuthService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val authService: JBotAuthService
) {



}