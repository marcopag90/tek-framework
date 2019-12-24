package it.jbot.core

import it.jbot.core.util.jbotTimestamp
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import java.util.*

/**
 * Extension for [org.springframework.http.ResponseEntity]
 *
 * This _MUST_ be sent to client only with ResponseEntity [HttpStatus] between 200 and 299
 */
class JBotBaseResponse(
    httpStatus: HttpStatus
) {

    constructor(httpStatus: HttpStatus, result: Any?) : this(httpStatus) {
        this.result = result
    }

    val timestamp: Date = Date().jbotTimestamp()
    val status: Int = httpStatus.value()
    var result: Any? = null
}

/**
 * Extension for [org.springframework.http.ResponseEntity].
 *
 * Returns a specific [Entity]
 *
 * This _MUST_ be sent to client only with ResponseEntity [HttpStatus] between 200 and 299
 */
class JBotResponseEntity<Entity>(
    httpStatus: HttpStatus
) {
    constructor(httpStatus: HttpStatus, result: Entity) : this(httpStatus) {
        this.result = result
    }

    val timestamp: Date = Date().jbotTimestamp()
    val status: Int = httpStatus.value()
    var result: Entity? = null
}

/**
 * Extension for [org.springframework.http.ResponseEntity].
 *
 * Returns a specific [org.springframework.data.domain.Page] of type [Entity]
 *
 * This _MUST_ be sent to client only with ResponseEntity [HttpStatus] between 200 and 299
 */
class JBotPageResponse<Entity>(
    httpStatus: HttpStatus
) {
    constructor(httpStatus: HttpStatus, result: Page<Entity>) : this(httpStatus) {
        this.result = result
    }

    val timestamp: Date = Date().jbotTimestamp()
    val status: Int = httpStatus.value()
    var result: Page<Entity>? = null
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
