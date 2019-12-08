package it.jbot.security.form

import it.jbot.security.i18n.SecurityMessageSource.Companion.validationEmail
import it.jbot.security.i18n.SecurityMessageSource.Companion.validationNotBlankPassword
import it.jbot.security.i18n.SecurityMessageSource.Companion.validationSizeEmail
import it.jbot.security.i18n.SecurityMessageSource.Companion.validationNotBlankEmail
import it.jbot.security.i18n.SecurityMessageSource.Companion.validationNotBlankUsername
import it.jbot.security.i18n.SecurityMessageSource.Companion.validationSizeUsername
import javax.annotation.RegEx
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class RegisterForm(

    @field:NotBlank(message = "{$validationNotBlankUsername}")
    @field:Size(min = 3, max = 20, message = "{$validationSizeUsername}")
    val username: String,

    @field:NotBlank(message = "{$validationNotBlankPassword}")
    val password: String,

    @field:Email(message = "{$validationEmail}")
    @field:NotBlank(message = "{$validationNotBlankEmail}")
    @field:Size(max = 50, message = "$validationSizeEmail}")
    val email: String,

    val roles: MutableSet<String> = mutableSetOf()
)
