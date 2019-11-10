package it.jbot.security.service

import it.jbot.security.dto.RegisterForm
import it.jbot.security.model.User

interface UserService {
    
    fun registerUser(registerForm: RegisterForm) : User
    
}