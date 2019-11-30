package it.jbot.core.util

import java.io.InputStream

/**
 * function to write _hasAuthority('[this]')_
 */
fun hasAuthority(authority: String): String = "hasAuthority('$authority')"

fun isAnonymous(): String = "isAnonymous()"

/**
 * Extension function to concat a logical OR condition on the called object, with the given _parameter_
 */
fun String.or(arg: String): String = this.plus(" || ").plus(arg)

/**
 * Extension function to concat a logical AND condition on the called object, with the given _parameter_
 */
fun String.and(arg: String): String = this.plus(" && ").plus(arg)

/**
 * Infix operator to execute a condition only if the given subject is _false_
 */
infix fun Boolean.ifNot(block: () -> Unit) {
    if (!this) block()
}

/**
 * Infix operator to execute a condition only if the given subject _is null_
 */
infix fun Any?.ifNull(block: () -> Unit) {
    if (this == null) block()
}

/**
 * Infix operator analogous to Kotlin's `takeIf` extension function
 * Only to be used with classNames or ClassNameSet objects
 */
infix fun String.applyIf(condition: Boolean): String? =
    if (condition) this else null

/**
 * Collection utility to execute a condition only if the given collection _is not null_
 */
inline fun <T, C : Collection<T>, O> C.ifNotEmpty(body: C.() -> O?): O? = if (isNotEmpty()) this.body() else null

/**
 * Array utility to execute a condition only if the given array _is not null_
 */
inline fun <T, O> Array<out T>.ifNotEmpty(body: Array<out T>.() -> O?): O? = if (isNotEmpty()) this.body() else null

/**
 * Shorthand for reading an InputStream as a String
 */
fun InputStream.readText(): String = reader().readText()

/**
 * Utility function with _development purpose_ to state that this part of code should not be reached
 *
 * (Good job if you can make it!)
 */
fun unreachableCode(): Nothing = throw NotReachableCode()

class NotReachableCode(message: String = "This line should be unreachable.") : Error(message)
