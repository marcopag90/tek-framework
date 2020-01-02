package com.tek.core.util

import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaField

/**
 * Collection utility to execute a condition only if the given collection _is not null_
 */
inline fun <T, C : Collection<T>, O> C.ifNotEmpty(body: C.() -> O?): O? = if (isNotEmpty()) this.body() else null

/**
 * Array utility to execute a condition only if the given array _is not null_
 */
inline fun <T, O> Array<out T>.ifNotEmpty(body: Array<out T>.() -> O?): O? = if (isNotEmpty()) this.body() else null

/**
 * Return all annotations defined on the property, both Kotlin and Java ones.
 */
@Suppress("UselessCallOnNotNull")
val <T : Any?> KProperty<T>.allAnnotations: List<Annotation>
    get() = annotations.orEmpty() + javaField?.annotations.orEmpty()

/**
 * Find the first item of the specified class in an Iterable
 */
inline fun <reified C> Iterable<*>.findInstanceOrNull(): C? =
    find(C::class::isInstance) as C?