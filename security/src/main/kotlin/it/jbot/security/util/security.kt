package it.jbot.security.util

import it.jbot.security.model.enums.PrivilegeName
import org.springframework.security.core.context.SecurityContextHolder

fun hasPrivilege(privilegeName: PrivilegeName): Boolean {
    val authentication = SecurityContextHolder.getContext().authentication
    return when (authentication.authorities.find { it.authority == privilegeName.name }) {
        null -> false
        else -> true
    }
}