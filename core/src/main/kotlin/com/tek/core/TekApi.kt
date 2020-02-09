package com.tek.core

import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import java.time.Instant

/**
 * Extension for [org.springframework.http.ResponseEntity]
 *
 * This _MUST_ be sent to client only with ResponseEntity [HttpStatus] between 200 and 299
 */
class TekBaseResponse(
    httpStatus: HttpStatus
) {

    constructor(httpStatus: HttpStatus, result: Any?) : this(httpStatus) {
        this.result = result
    }

    val timestamp: Instant = Instant.now()
    val status: Int = httpStatus.value()
    var result: Any? = null
}

/**
 * Extension for [org.springframework.http.ResponseEntity].
 *
 * Returns a specific entity of type [E]
 *
 * This _MUST_ be sent to client only with ResponseEntity [HttpStatus] between 200 and 299
 */
class TekResponseEntity<E>(
    httpStatus: HttpStatus
) {
    constructor(httpStatus: HttpStatus, result: E) : this(httpStatus) {
        this.result = result
    }

    val timestamp: Instant = Instant.now()
    val status: Int = httpStatus.value()
    var result: E? = null
}


/**
 * Extension for [org.springframework.http.ResponseEntity].
 *
 * Returns a specific [org.springframework.data.domain.Page] of type [E]
 *
 * This _MUST_ be sent to client only with ResponseEntity [HttpStatus] between 200 and 299
 */

class TekPageResponse<E>(
    httpStatus: HttpStatus,
    page: Page<E>
) {

    data class TekPageResult<E>(
        val content: List<E>,
        val totalElements: Long,
        val totalPages: Int
    )

    val result: TekPageResult<E> = TekPageResult(page.content, page.totalElements, page.totalPages)
    val timestamp: Instant = Instant.now()
    val status: Int = httpStatus.value()
}

/**
 * Extension for [org.springframework.http.ResponseEntity]
 *
 * This _MUST_ be returned to client only with ResponseEntity [HttpStatus] over 299 (300+, 400+, 500+)
 */
class TekErrorResponse(
    httpStatus: HttpStatus
) {

    val timestamp: Instant = Instant.now()
    val status: Int = httpStatus.value()
    var errors: Map<String, String?> = mutableMapOf()
    var path: String? = null
}
