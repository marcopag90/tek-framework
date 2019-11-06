package it.jbot.shared

import it.jbot.shared.util.JBotDateUtils.jbotTimestamp
import org.springframework.http.HttpStatus
import java.util.*

/**
 * Extension for [org.springframework.http.ResponseEntity]
 *
 * This _MUST_ be returned to client only with ResponseEntity [HttpStatus] between 200 and 299
 */
class JBotResponse {
    private var timestamp: Date = jbotTimestamp()
    var result: Any? = null
}

/**
 * Extension for [org.springframework.http.ResponseEntity]
 *
 * This _MUST_ be returned to client only with ResponseEntity [HttpStatus] over 299 (300+, 400+, 500+)
 */
class JBotErrorResponse {
    private var timestamp: Date = jbotTimestamp()
    var errors: List<String?> = mutableListOf()
    var path: String? = null
}