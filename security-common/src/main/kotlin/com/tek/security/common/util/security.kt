package com.tek.security.common.util

import com.tek.security.common.model.RoleName
import org.springframework.security.core.context.SecurityContextHolder

fun hasRole(roleName: RoleName): Boolean {
    val authentication = SecurityContextHolder.getContext().authentication
    return when (authentication.authorities.find { it.authority == roleName.name }) {
        null -> false
        else -> true
    }
}

fun hasAuthority(authority: String): String = "hasAuthority('$authority')"

fun isAnonymous(): String = "isAnonymous()"

fun String.antPath() = "$this/**"