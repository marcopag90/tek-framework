package com.tek.form

import com.tek.security.common.form.AbstractRegisterForm
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class AppRegisterForm(
    @field:NotBlank
    @field:Size(min = 3, max = 20)
    override val username: String,

    @field:NotBlank
    override val password: String,

    @field:NotBlank
    @field:Email
    override val email: String?
) : AbstractRegisterForm()

