package it.jbot.shared.exception

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

/**
 * Custom Exception that _MUST_ be thrown only in Business Logic failures
 */
class JBotServiceException : RuntimeException {

    constructor(message: String, httpStatus: HttpStatus) : super(message) {
        this.httpStatus = httpStatus
    }
    
    constructor(message: String , httpStatus: HttpStatus, cause: Throwable) : super(message, cause) {
        this.httpStatus = httpStatus
    }
    
    var httpStatus: HttpStatus
}