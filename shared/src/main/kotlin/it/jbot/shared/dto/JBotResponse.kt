package it.jbot.shared.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.http.HttpStatus
import java.util.*

class JBotResponse(
    @JsonIgnoreProperties
    var httpStatus: HttpStatus
) {
    var timestamp: Date? = null
    var status: Int = httpStatus.value()
    var result: Any? = null
}

class JBotErrorResponse(
    @JsonIgnoreProperties
    var httpStatus: HttpStatus
) {
    var timestamp: Date = Date()
    var status: Int = httpStatus.value()
    var errors: Array<String?> = arrayOf()
}