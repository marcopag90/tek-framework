package it.jbot.security.service

import it.jbot.security.dto.SignUpForm
import it.jbot.security.model.User

interface UserService {
    
    fun registerUser(signUpForm: SignUpForm) : User
    
}