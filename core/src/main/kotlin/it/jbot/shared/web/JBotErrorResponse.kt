package it.jbot.shared.web

import it.jbot.shared.util.JBotDateUtils.jbotTimestamp
import org.springframework.http.HttpStatus
import java.util.*

/**
 * Extension for [org.springframework.http.ResponseEntity]
 *
 * This _MUST_ be returned to client only with ResponseEntity [HttpStatus] over 299 (300+, 400+, 500+)
 */
class JBotErrorResponse(
    httpStatus: HttpStatus
) {
    val timestamp: Date = jbotTimestamp()
    val status: Int = httpStatus.value()
    var errors: List<String?> = mutableListOf()
    var path: String? = null
}