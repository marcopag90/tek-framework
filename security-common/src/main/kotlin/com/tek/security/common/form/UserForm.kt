package com.tek.security.common.form

import com.tek.core.controller.CreatableForm
import com.tek.core.controller.UpdatableForm
import com.tek.security.common.model.TekProfile
import java.io.Serializable
import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserCreateForm(
    @field:Size(min = 3, max = 20)
    val username: String,
    val profiles: Set<TekProfile>
) : CreatableForm()

data class UserUpdateForm(
    @field:Size(min = 3, max = 20)
    val username: String?,
    val password: String?,
    val expireAt: LocalDate?,
    val enabled: Boolean?,
    val profiles: Set<TekProfile>
) : UpdatableForm()

data class ChangeEmailForm(
    @field: NotBlank
    @field: Size(min = 1, max = 50)
    @field:Email
    val oldEmail: String,

    @field: NotBlank
    @field: Size(min = 1, max = 50)
    @field:Email
    val newEmail: String
)

data class ChangePasswordForm(
    @field: NotBlank val oldPassword: String,
    @field: NotBlank val newPassword: String
)

abstract class AbstractRegisterForm : Serializable {
    abstract val username: String
    abstract val password: String
    abstract val email: String?
}