package it.jbot.shared.exception

import it.jbot.shared.JBotErrorResponse
import it.jbot.shared.util.LoggerDelegate
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
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<JBotErrorResponse> {
        
        log.error(ex.message, ex)
        
        return ResponseEntity<JBotErrorResponse>(
            JBotErrorResponse().apply {
                this.errors = arrayListOf(ex.message)
                //TODO check if this gives the same behaviour of JBotOAuth2AccessDeniedHandler path
                this.path = request.contextPath
            },
            HttpStatus.INTERNAL_SERVER_ERROR
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
        
        log.error(ex.message, ex)
        
        return ResponseEntity<Any>(
            
            JBotErrorResponse().apply {
                this.errors =
                    ex.bindingResult.fieldErrors.stream().map { e ->
                        e.defaultMessage
                    }.collect(Collectors.toList())
                //TODO check if this gives the same behaviour of JBotOAuth2AccessDeniedHandler path
                this.path = request.contextPath
            },
            headers,
            status
        )
    }
}