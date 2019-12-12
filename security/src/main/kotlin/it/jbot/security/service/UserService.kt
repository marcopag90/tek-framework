package it.jbot.security.service

import it.jbot.core.service.CrudService
import it.jbot.security.form.RegisterForm
import it.jbot.security.model.User

interface UserService : CrudService<User, Long> {

    fun register(registerForm: RegisterForm): User

}