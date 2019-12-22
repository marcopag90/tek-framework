package it.jbot.security.service

import it.jbot.security.form.RegisterForm
import it.jbot.security.model.JBotUser

interface UserService {

    fun register(registerForm: RegisterForm): JBotUser

}