package com.tek.core.exception

import com.tek.core.TekErrorResponse
import com.tek.core.i18n.CoreMessageSource
import com.tek.core.i18n.CoreMessageSource.Companion.messageInternalServerError
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
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
import java.io.StringWriter
import kotlin.reflect.jvm.internal.impl.utils.ExceptionUtilsKt

/**
 * [org.springframework.web.bind.annotation.RestController] exception handler
 */
@ControllerAdvice
class TekExceptionHandler(
    private val coreMessageSource: CoreMessageSource
) : ResponseEntityExceptionHandler() {

    private val log: Logger = LoggerFactory.getLogger(TekExceptionHandler::class.java)

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
            TekErrorResponse(HttpStatus.BAD_REQUEST).apply {
                this.errors = mapOf("error" to ex.message)
                this.path = (request as ServletWebRequest).request.servletPath
            },
            HttpStatus.BAD_REQUEST
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

        val httpStatus = HttpStatus.NOT_ACCEPTABLE

        return ResponseEntity(
            TekErrorResponse(httpStatus).apply {
                this.errors = ex.bindingResult.allErrors.associate {
                    (it as FieldError).field to it.defaultMessage
                }
                this.path = (request as ServletWebRequest).request.servletPath
            },
            headers,
            httpStatus
        )
    }

    /**
     * Function to give a standard response for a [TekServiceException]
     */
    @ExceptionHandler(TekServiceException::class)
    fun handleServiceException(
        ex: TekServiceException,
        request: WebRequest
    ): ResponseEntity<TekErrorResponse> {

        log.warn(ex.message)

        return ResponseEntity(
            TekErrorResponse(ex.httpStatus).apply {
                this.errors = mapOf("error" to ex.message)
                this.path = (request as ServletWebRequest).request.servletPath
            },
            ex.httpStatus
        )
    }

    /**
     * Function to give a standard response for a [TekResourceNotFoundException]
     */
    @ExceptionHandler(TekResourceNotFoundException::class)
    fun handleServiceException(
        ex: TekResourceNotFoundException,
        request: WebRequest
    ): ResponseEntity<TekErrorResponse> {

        log.warn(ex.message)

        return ResponseEntity(
            TekErrorResponse(ex.httpStatus).apply {
                this.errors = mapOf("error" to ex.message)
                this.path = (request as ServletWebRequest).request.servletPath
            },
            ex.httpStatus
        )
    }

    /**
     * Function to give a standard response for a [TekValidationException]
     */
    @ExceptionHandler(TekValidationException::class)
    fun handleValidationException(
        ex: TekValidationException,
        request: WebRequest
    ): ResponseEntity<TekErrorResponse> {

        val httpStatus = HttpStatus.NOT_ACCEPTABLE
        return ResponseEntity(
            TekErrorResponse(httpStatus).apply {
                this.errors = ex.errors
                this.path = (request as ServletWebRequest).request.servletPath
            },
            httpStatus
        )
    }

    @ExceptionHandler(Exception::class)
    @Order(Ordered.LOWEST_PRECEDENCE)
    fun handleGenericException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<TekErrorResponse> {

        log.error(ExceptionUtils.getStackTrace(ex))

        val error = coreMessageSource.getCoreMessageSource()
            .getMessage(messageInternalServerError, null, LocaleContextHolder.getLocale())

        val httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        return ResponseEntity(
            TekErrorResponse(httpStatus).apply {
                this.errors = mapOf("error" to error)
                this.path = (request as ServletWebRequest).request.servletPath
            },
            httpStatus
        )
    }
}