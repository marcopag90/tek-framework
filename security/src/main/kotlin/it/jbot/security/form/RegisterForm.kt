package it.jbot.security.form

import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class RegisterForm(

    @field:NotBlank
    @field:Size(min = 3, max = 20)
    val username: String,

    @field:NotBlank
    val password: String,

    @field:NotBlank
    @field:Email
    val email: String
)
