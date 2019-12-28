package com.tek.core.exception

/**
 * Custom Exception that _MUST_ be thrown only in Business Logic Validation failures
 */
class TekValidationException(var errors: MutableMap<String, String>) : RuntimeException() {

}