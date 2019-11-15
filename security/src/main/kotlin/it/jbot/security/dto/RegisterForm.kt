package it.jbot.security.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class RegisterForm(
    
    @NotBlank
    @Size(min = 3, max = 20)
    val username: String,
    
    @NotBlank
    @Size(min = 8)
    val password: String,
    
    @Email
    @NotBlank
    @Size(max = 50)
    val email: String,
    
    val roles: MutableSet<String> = mutableSetOf()
)