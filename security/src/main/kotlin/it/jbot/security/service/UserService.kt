package it.jbot.security.service

import it.jbot.core.service.CrudService
import it.jbot.security.form.RegisterForm

interface UserService<User> : CrudService<User> {

    fun register(registerForm: RegisterForm): User

}