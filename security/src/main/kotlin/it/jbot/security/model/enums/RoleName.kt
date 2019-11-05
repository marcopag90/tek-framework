package it.jbot.security.model.enums

import it.jbot.shared.util.LabelEnum

enum class RoleName(override val label: String) :
    LabelEnum {
    
    ROLE_ADMIN("Administrator"),
    ROLE_USER("User")
}