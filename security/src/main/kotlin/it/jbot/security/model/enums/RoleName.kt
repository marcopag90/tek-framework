package it.jbot.security.model.enums

import it.jbot.core.util.LabelEnum

enum class RoleName(
    override val label: String
) : LabelEnum {

    ADMIN("Administrator"),
    USER("User")
}