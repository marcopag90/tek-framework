package com.tek.security.common.form

import com.tek.core.form.AbstractForm
import javax.validation.constraints.NotBlank

data class ProfileForm(
    @NotBlank
    val roles: Set<String>
) : AbstractForm()