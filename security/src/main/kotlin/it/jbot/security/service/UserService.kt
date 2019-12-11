package it.jbot.security.service

import it.jbot.core.web.UpdatableService
import it.jbot.security.form.RegisterForm

interface UserService<User> : UpdatableService<User> {

    fun register(registerForm: RegisterForm): User

}