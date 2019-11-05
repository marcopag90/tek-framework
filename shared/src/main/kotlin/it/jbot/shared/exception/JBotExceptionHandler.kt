package it.jbot.shared.exception

import it.jbot.shared.dto.JBotErrorResponse
import it.jbot.shared.util.LoggerDelegate
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*
import java.util.stream.Collectors

@ControllerAdvice
class JBotExceptionHandler : ResponseEntityExceptionHandler() {
    
    private val log by LoggerDelegate()
    
    /**
     * Function to give a standard a response for a [JBotServiceException]
     */
    @ExceptionHandler(JBotServiceException::class)
    fun handleServiceException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<JBotErrorResponse> {
        
        log.error(ex.message, ex)
        
        var response: JBotErrorResponse =
            JBotErrorResponse(HttpStatus.BAD_REQUEST).apply {
                errors = arrayOf(ex.message)
            }
        
        return ResponseEntity<JBotErrorResponse>(response, response.httpStatus)
    }
    
    /**
     * Function to give a standard response for [MethodArgumentNotValidException]
     */
    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        
        log.error(ex.message, ex)
        
        var body = linkedMapOf<String, Any>()
        
        body[JBotErrorResponse::timestamp.name] = Date()
        body[JBotErrorResponse::status.name] = status.value()
        body[JBotErrorResponse::errors.name] =
            ex.bindingResult.fieldErrors.stream().map { e ->
                e.defaultMessage
            }.collect(Collectors.toList())
        
        return ResponseEntity<Any>(body, headers, status)
    }
}