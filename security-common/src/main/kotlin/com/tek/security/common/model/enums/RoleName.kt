package com.tek.security.common.model.enums

import com.tek.core.util.LabelEnum

enum class RoleName(
    override val label: String
) : LabelEnum {

    ADMIN("Administrator"),
    AUDIT("Auditor"),
    USER("User")
}