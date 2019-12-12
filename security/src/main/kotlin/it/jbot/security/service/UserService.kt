package it.jbot.security.service

import it.jbot.core.service.CrudService
import it.jbot.security.form.RegisterForm
import it.jbot.security.model.JBotUser

interface UserService : CrudService<JBotUser, Long> {

    fun register(registerForm: RegisterForm): JBotUser

}