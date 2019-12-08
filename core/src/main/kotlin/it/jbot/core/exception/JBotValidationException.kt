package it.jbot.core.exception

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

/**
 * Custom Exception that _MUST_ be thrown only in Business Logic Validation failures
 */
class JBotValidationException : RuntimeException {

    var errors: MutableMap<String, String?>

    constructor(
        errors: MutableMap<String, String?>
    ) : super() {
        this.errors = errors
    }
}