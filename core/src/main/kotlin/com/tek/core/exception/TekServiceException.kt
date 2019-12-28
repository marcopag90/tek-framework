package com.tek.core.exception

import com.tek.core.i18n.TekMessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus

/**
 * Custom Exception that _MUST_ be thrown only in Business Logic failures
 */
class TekServiceException : RuntimeException {

    constructor(
        message: String,
        httpStatus: HttpStatus
    ) : super(message) {
        this.httpStatus = httpStatus
    }

    constructor(
        data: ServiceExceptionData,
        httpStatus: HttpStatus
    ) : super(data.exceptionMessage) {
        this.httpStatus = httpStatus
    }

    constructor(
        data: ServiceExceptionData,
        httpStatus: HttpStatus,
        cause: Throwable
    ) : super(data.exceptionMessage, cause) {
        this.httpStatus = httpStatus
    }

    var httpStatus: HttpStatus
}

data class ServiceExceptionData(
    val source: TekMessageSource,
    val message: String,
    val parameters: Array<String>? = null
) {

    val exceptionMessage: String

    init {
        exceptionMessage = resolveMessage(this)
    }

    private fun resolveMessage(data: ServiceExceptionData): String {

        return data.source.getResource().getMessage(
            data.message,
            data.parameters,
            LocaleContextHolder.getLocale()
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ServiceExceptionData

        if (source != other.source) return false
        if (message != other.message) return false
        if (parameters != null) {
            if (other.parameters == null) return false
            if (!parameters.contentEquals(other.parameters)) return false
        } else if (other.parameters != null) return false
        if (exceptionMessage != other.exceptionMessage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + (parameters?.contentHashCode() ?: 0)
        result = 31 * result + exceptionMessage.hashCode()

        return result
    }

}