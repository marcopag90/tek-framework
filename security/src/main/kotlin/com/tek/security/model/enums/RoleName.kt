package com.tek.security.model.enums

import com.tek.core.util.LabelEnum

enum class RoleName(
    override val label: String
) : LabelEnum {

    ADMIN("Administrator"),
    USER("User")
}