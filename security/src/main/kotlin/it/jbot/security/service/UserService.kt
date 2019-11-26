package it.jbot.security.service

import it.jbot.security.dto.RegisterForm
import it.jbot.security.model.User

interface UserService {

    fun register(registerForm: RegisterForm): User

}