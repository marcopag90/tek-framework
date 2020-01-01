package com.tek.core.exception

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

/**
 * Custom Exception that _MUST_ be thrown when a Resource is not found in Repository
 */
class TekResourceNotFoundException(data: ServiceExceptionData) : RuntimeException(data.exceptionMessage) {

    val httpStatus: HttpStatus = HttpStatus.NOT_FOUND
}