package it.jbot.security.form

import it.jbot.core.form.AbstractDTO
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserForm(

    @field:NotBlank
    @field:Size(min = 3, max = 20)
    val username: String?,

    @field:NotBlank
    @field:Email
    val email: String?

) : AbstractDTO()