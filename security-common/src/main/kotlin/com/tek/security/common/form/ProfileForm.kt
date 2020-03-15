package com.tek.security.common.form

import com.tek.core.form.AbstractForm
import com.tek.security.common.model.RoleName
import com.tek.security.common.model.TekRole
import javax.validation.constraints.Size

data class ProfileForm(

    val roles: Set<TekRole>,

    @field:Size(min = 1, max = 10)
    val name: String?
) : AbstractForm()