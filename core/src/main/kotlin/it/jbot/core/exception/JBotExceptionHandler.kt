package it.jbot.core.exception

import it.jbot.core.JBotErrorResponse
import it.jbot.core.i18n.CoreMessageSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*

/**
 * [org.springframework.web.bind.annotation.RestController] exception handler
 */
@ControllerAdvice
class JBotExceptionHandler : ResponseEntityExceptionHandler() {

    private val log: Logger = LoggerFactory.getLogger(JBotExceptionHandler::class.java)

    /**
     * Function to give a standard response for a [HttpMessageNotReadableException]
     */
    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {

        log.warn(ex.message)

        return ResponseEntity(
            JBotErrorResponse(HttpStatus.BAD_REQUEST).apply {
                this.errors = mapOf("error" to ex.message)
                this.path = (request as ServletWebRequest).request.servletPath
            },
            HttpStatus.BAD_REQUEST
        )
    }

    /**
     * Function to give a standard response for a [JBotServiceException]
     */
    @ExceptionHandler(JBotServiceException::class)
    fun handleServiceException(
        ex: JBotServiceException,
        request: WebRequest
    ): ResponseEntity<JBotErrorResponse> {

        log.warn(ex.message)

        return ResponseEntity(
            JBotErrorResponse(ex.httpStatus).apply {
                this.errors = mapOf("error" to ex.message)
                this.path = (request as ServletWebRequest).request.servletPath
            },
            ex.httpStatus
        )
    }

    /**
     * Function to give a standard response for a [JBotValidationException]
     */
    @ExceptionHandler(JBotValidationException::class)
    fun handleValidationException(
        ex: JBotValidationException,
        request: WebRequest
    ): ResponseEntity<JBotErrorResponse> {

        log.warn(ex.message)

        val status = HttpStatus.NOT_ACCEPTABLE

        return ResponseEntity(
            JBotErrorResponse(status).apply {
                this.errors = ex.errors
                this.path = (request as ServletWebRequest).request.servletPath
            },
            status
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

        log.warn(ex.message)

        val status = HttpStatus.NOT_ACCEPTABLE

        return ResponseEntity(
            JBotErrorResponse(status).apply {
                this.errors = ex.bindingResult.allErrors.associate {
                    (it as FieldError).field to it.defaultMessage
                }
                this.path = (request as ServletWebRequest).request.servletPath
            },
            headers,
            status
        )
    }
}