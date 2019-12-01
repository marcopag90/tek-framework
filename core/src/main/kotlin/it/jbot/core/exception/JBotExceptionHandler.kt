package it.jbot.core.exception

import it.jbot.core.JBotErrorResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class JBotExceptionHandler : ResponseEntityExceptionHandler() {

    private val log: Logger = LoggerFactory.getLogger(JBotExceptionHandler::class.java)

    /**
     * Function to give a standard response for a [JBotServiceException]
     */
    @ExceptionHandler(JBotServiceException::class)
    fun handleServiceException(
        ex: JBotServiceException,
        request: WebRequest
    ): ResponseEntity<JBotErrorResponse> {

        return ResponseEntity(
            JBotErrorResponse().apply {
                this.errors = mapOf("message" to ex.message)
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

        //TODO validazione i18n!

        log.warn(ex.message)

        return ResponseEntity(

            JBotErrorResponse().apply {
                this.errors =
                    ex.bindingResult.fieldErrors.associate { it.field to it.defaultMessage }
                this.path = (request as ServletWebRequest).request.servletPath
            },
            headers,
            status
        )
    }
}