package it.jbot.security.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class RegisterForm(

    @field:NotBlank
    @field:Size(min = 3, max = 20)
    val username: String,

    @field:NotBlank
    @field:Size(min = 8)
    val password: String,

    @field:Email
    @field:NotBlank
    @field:Size(max = 50)
    val email: String,

    val roles: MutableSet<String> = mutableSetOf()
)