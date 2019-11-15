package it.jbot.security.model.enums

import it.jbot.shared.exception.JBotServiceException
import it.jbot.shared.util.LabelEnum
import org.springframework.http.HttpStatus
import kotlin.reflect.KClass

enum class RoleName(override val label: String) : LabelEnum {
    
    ROLE_ADMIN("Administrator"),
    ROLE_USER("User");
    
    companion object {
        fun fromString(name: String): RoleName {
            return when (name) {
                ROLE_ADMIN.name -> ROLE_ADMIN
                ROLE_USER.name -> ROLE_USER
                else -> throw JBotServiceException(
                    "RoleName: $name not found!",
                    HttpStatus.NOT_FOUND
                )
            }
        }
    }
}