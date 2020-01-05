package com.tek.security.form

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class ContactForm(

    @field:NotBlank
    val name: String,

    @field:NotBlank
    val surname: String,

    val company: String?,

    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    val message: String
)