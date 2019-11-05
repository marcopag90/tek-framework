package it.jbot.shared.exception

import java.lang.RuntimeException

/**
 * Custom Exception to be thrown into Business Logic
 */
class JBotServiceException : RuntimeException {

    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}