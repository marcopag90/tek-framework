package it.jbot.core

import it.jbot.core.util.jbotTimestamp
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import java.util.*

/**
 * Extension for [org.springframework.http.ResponseEntity]
 *
 * This _MUST_ be returned to client only with ResponseEntity [HttpStatus] between 200 and 299
 */
class JBotResponse(
    httpStatus: HttpStatus
) {

    constructor(httpStatus: HttpStatus, result: Any?) : this(httpStatus) {
        this.result = result
    }

    val timestamp: Date = Date().jbotTimestamp()
    val status: Int = httpStatus.value()
    var result: Any? = null
}

class JBotPageEntityResponse<T>(
    httpStatus: HttpStatus
) {
    constructor(httpStatus: HttpStatus, result: Page<T>) : this(httpStatus) {
        this.result = result
    }

    val timestamp: Date = Date().jbotTimestamp()
    val status: Int = httpStatus.value()
    var result: Page<T>? = null
}

/**
 * Extension for [org.springframework.http.ResponseEntity]
 *
 * This _MUST_ be returned to client only with ResponseEntity [HttpStatus] over 299 (300+, 400+, 500+)
 */
class JBotErrorResponse(
    httpStatus: HttpStatus
) {

    val timestamp: Date = Date().jbotTimestamp()
    val status: Int = httpStatus.value()
    var errors: Map<String, String?> = mutableMapOf()
    var path: String? = null
}
