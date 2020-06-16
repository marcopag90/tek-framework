package com.tek.security.common

import org.springframework.security.core.context.SecurityContextHolder

/**
 * Annotation to place over an Entity to specify the authority prefix.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RolePrefix(
    val value: String = "",
    val enabled: Boolean = true
)

fun hasRole(role: String): Boolean {
    val authentication = SecurityContextHolder.getContext().authentication
    require(authentication != null) {
        "No authentication provided"
    }
    check(authentication.isAuthenticated) {
        "User not authenticated!"
    }
    return when (authentication.authorities.find { it.authority == role }) {
        null -> false
        else -> true
    }
}

fun hasAuthority(authority: String): String = "hasAuthority('$authority')"

fun isAnonymous(): String = "isAnonymous()"

fun String.antPath() = "$this/**"

