package com.tek.security.util

import com.tek.security.model.enums.PrivilegeName
import org.springframework.security.core.context.SecurityContextHolder

fun hasPrivilege(privilegeName: PrivilegeName): Boolean {
    val authentication = SecurityContextHolder.getContext().authentication
    return when (authentication.authorities.find { it.authority == privilegeName.name }) {
        null -> false
        else -> true
    }
}

fun hasAuthority(authority: String): String = "hasAuthority('$authority')"

fun isAnonymous(): String = "isAnonymous()"

fun String.antPath() = "$this/**"