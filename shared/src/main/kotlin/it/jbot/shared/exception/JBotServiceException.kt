package it.jbot.shared.exception

import java.lang.RuntimeException

/**
 * Custom Exception that _MUST_ be thrown only in Business Logic failures
 */
class JBotServiceException : RuntimeException {

    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}