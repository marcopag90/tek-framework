package it.jbot.security.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class RegisterForm {
    
    @NotBlank
    @Size(min = 3, max = 20)
    lateinit var username: String
    
    @NotBlank
    @Size(min = 8)
    lateinit var password: String
    
    @Email
    @NotBlank
    @Size(max = 50)
    lateinit var email: String
    
    var roles: MutableSet<String> = mutableSetOf()
}