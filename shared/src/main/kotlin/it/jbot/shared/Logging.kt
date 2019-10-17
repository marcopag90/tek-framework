package it.jbot.shared

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.companionObject

interface Logging

fun <T : Any> getClassForLogging(javaClass: Class<T>): Class<*> {

    return javaClass.enclosingClass?.takeIf {
        it.kotlin.companionObject?.java == javaClass
    } ?: javaClass
}

inline fun <reified T : Logging> T.logger(): Logger = getLogger(getClassForLogging(T::class.java))

class LoggerDelegate<in R : Any> : ReadOnlyProperty<R, Logger> {
    override fun getValue(thisRef: R, property: KProperty<*>): Logger = getLogger(getClassForLogging(thisRef.javaClass))
}
