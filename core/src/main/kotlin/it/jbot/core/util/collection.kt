package it.jbot.core.util

/**
 * Collection utility to execute a condition only if the given collection _is not null_
 */
inline fun <T, C : Collection<T>, O> C.ifNotEmpty(body: C.() -> O?): O? = if (isNotEmpty()) this.body() else null

/**
 * Array utility to execute a condition only if the given array _is not null_
 */
inline fun <T, O> Array<out T>.ifNotEmpty(body: Array<out T>.() -> O?): O? = if (isNotEmpty()) this.body() else null