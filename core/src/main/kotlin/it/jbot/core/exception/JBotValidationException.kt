package it.jbot.core.exception

/**
 * Custom Exception that _MUST_ be thrown only in Business Logic Validation failures
 */
class JBotValidationException(var errors: MutableMap<String, String>) : RuntimeException() {

}