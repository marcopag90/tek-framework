package com.tek.security.common.form

import com.tek.core.controller.CreatableForm
import com.tek.core.controller.UpdatableForm
import com.tek.security.common.model.TekRole
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class ProfileCreateForm(
    val roles: MutableSet<TekRole>,
    @field:NotBlank
    @field:Size(min = 1, max = 10)
    val name: String?
) : CreatableForm()

data class ProfileUpdateForm(
    val roles: MutableSet<TekRole>,
    @field:Size(min = 1, max = 10)
    val name: String?
) : UpdatableForm()