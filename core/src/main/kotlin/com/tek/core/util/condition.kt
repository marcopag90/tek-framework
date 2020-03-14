package com.tek.core.util

import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.context.annotation.Conditional
import org.springframework.core.type.AnnotatedTypeMetadata
import java.util.*

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Conditional(ConditionalOnMissingProperty.OnNullProperty::class)
annotation class ConditionalOnMissingProperty(
    val value: String
) {
    class OnNullProperty : Condition {
        override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
            val attributes =
                metadata.getAnnotationAttributes(ConditionalOnMissingProperty::class.java.name)!!
            val value: String? = context.environment.getProperty(attributes["value"] as String)
            return (value == null)
        }
    }
}

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
infix fun Boolean.isFalse(block: () -> Unit) {
    if (!this) block()
}

/**
 * Infix operator to execute a condition only if the given subject is true
 */
infix fun Boolean.isTrue(block: () -> Unit) {
    if (this) block()
}

/**
 * Infix operator to execute a condition only if the given subject _is null_
 */
infix fun Any?.ifNull(block: () -> Unit) {
    if (this == null) block()
}

/**
 * Shorthand to convert an Optional to a nullable type.
 */
fun <T> Optional<T>.orNull(): T? = orElse(null)


/**
 * Infix operator analogous to Kotlin's `takeIf` extension function
 * Only to be used with classNames or ClassNameSet objects
 */
infix fun String.applyIf(condition: Boolean): String? =
    if (condition) this else null

/**
 * Utility function with _development purpose_ to state that this part of code should not be reached
 *
 * (Good job if you can make it!)
 */
fun unreachableCode(): Nothing = throw NotReachableCode()

class NotReachableCode(message: String = "This line should be unreachable.") : Error(message)

/**
 * Utility function with _development purpose_ to state that this part of code is not supported
 */
fun notSupported(): Nothing = throw NotSupported()

class NotSupported(message: String = "This method is not supported.") : Error(message)

/**
 * Utility function to act like nothing happened
 */
fun doNothing() = Unit

