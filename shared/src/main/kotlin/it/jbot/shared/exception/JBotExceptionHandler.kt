package it.jbot.shared.exception

import it.jbot.shared.util.LoggerDelegate
import it.jbot.shared.web.JBotErrorResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.stream.Collectors

@ControllerAdvice
class JBotExceptionHandler : ResponseEntityExceptionHandler() {
    
    private val log by LoggerDelegate()
    
    /**
     * Function to give a standard response for a [JBotServiceException]
     */
    @ExceptionHandler(JBotServiceException::class)
    fun handleServiceException(
        ex: JBotServiceException,
        request: WebRequest
    ): ResponseEntity<JBotErrorResponse> {
        
        log.error(ex.message)
        
        return ResponseEntity(
            JBotErrorResponse(ex.httpStatus).apply {
                this.errors = arrayListOf(ex.message)
                this.path = (request as ServletWebRequest).request.servletPath
            },
            ex.httpStatus
        )
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
        
        log.error(ex.message)
        
        return ResponseEntity(
            
            JBotErrorResponse(status).apply {
                this.errors =
                    ex.bindingResult.fieldErrors.stream().map { e ->
                        e.defaultMessage
                    }.collect(Collectors.toList())
                this.path = (request as ServletWebRequest).request.servletPath
            },
            headers,
            status
        )
    }
}