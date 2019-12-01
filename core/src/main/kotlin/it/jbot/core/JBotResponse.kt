package it.jbot.core

import it.jbot.core.util.JBotDateUtils.jbotTimestamp
import org.springframework.http.HttpStatus
import java.util.*

/**
 * Extension for [org.springframework.http.ResponseEntity]
 *
 * This _MUST_ be returned to client only with ResponseEntity [HttpStatus] between 200 and 299
 */
class JBotResponse(var result: Any?) {
    val timestamp: Date = jbotTimestamp()
}

/**
 * Extension for [org.springframework.http.ResponseEntity]
 *
 * This _MUST_ be returned to client only with ResponseEntity [HttpStatus] over 299 (300+, 400+, 500+)
 */
class JBotErrorResponse {
    val timestamp: Date = jbotTimestamp()
    var errors: Map<Any, String?> = mutableMapOf()
    var path: String? = null
}
