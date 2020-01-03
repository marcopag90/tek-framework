package com.tek.core.web.qdsl

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * Permit to transform a String or Int value into the specified type.
 */
@Component
class JsonTypeConverter(
    private val objectMapper: ObjectMapper
) {
    /**
     * Convert the specified string into the specified type
     *
     * @request value the string value
     * @request type expected type
     * @return the converted object
     */
    fun <T> convert(value: String, type: Class<T>): T {
        return doConvert(value, type)
    }

    /**
     * Convert the specified integer into the specified type
     *
     * @request value the integer value
     * @request type expected type
     * @return the converted object
     */
    @Suppress("UNUSED")
    fun <T> convert(value: Int, type: Class<T>): T {
        return doConvert(value, type)
    }

    /**
     * Convert the specified object into the specified type
     *
     * @request value the object value
     * @request type expected type
     * @return the converted object
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> doConvert(value: Any, type: Class<T>): T { // nothing to do - it is the good type
        if (value.javaClass == type) {
            return value as T
        }
        // date converter
        if (value is String) {
            when (type) {
                Date::class.java -> value as T
                LocalDate::class.java -> LocalDate.parse(value) as T
                LocalDateTime::class.java -> LocalDateTime.parse(value) as T
                UUID::class.java -> UUID.fromString(value.toString()) as T
                else -> throw NotImplementedError("Missing type implementation")
            }
        }
        return objectMapper.convertValue(value, type)
    }
}


