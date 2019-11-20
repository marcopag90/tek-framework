package it.jbot.shared.exception

import it.jbot.shared.i18n.JBotMessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import java.lang.RuntimeException

/**
 * Custom Exception that _MUST_ be thrown only in Business Logic failures
 */
class JBotServiceException : RuntimeException {
    
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
    val source: JBotMessageSource,
    val message: String,
    val parameters: Array<String>
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
        if (!parameters.contentEquals(other.parameters)) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + parameters.contentHashCode()
        return result
    }
}