package com.tek.core.i18n

import com.tek.core.TekBaseResponse
import com.tek.core.i18n.CoreMessageSource.Companion.localeLang
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiParam
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val LOCALE_PATTERN = "/locale"

@RestController
@RequestMapping(LOCALE_PATTERN)
class LocaleController(
    private val messageSource: CoreMessageSource
) {

    @GetMapping
    @ApiImplicitParam(
        name = "lang",
        dataType = "string",
        paramType = "query",
        defaultValue = "en",
        example = "en",
        value = "Value of the locale to set."
    )
    fun getCurrentLocale(): ResponseEntity<TekBaseResponse> =
        ResponseEntity(
            TekBaseResponse(
                HttpStatus.OK,
                messageSource.getCoreMessageSource().getMessage(localeLang, null, LocaleContextHolder.getLocale())
            ),
            HttpStatus.OK
        )
}


