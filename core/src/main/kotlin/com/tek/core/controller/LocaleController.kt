package com.tek.core.controller

import com.tek.core.LOCALE_PATH
import com.tek.core.TekBaseResponse
import com.tek.core.i18n.CoreMessageSource
import com.tek.core.i18n.CoreMessageSource.Companion.localeLang
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@Api(tags = ["Locale"])
@RestController
@RequestMapping(path = [LOCALE_PATH], produces = [MediaType.APPLICATION_JSON_VALUE])
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
        ResponseEntity.ok(
            TekBaseResponse(
                HttpStatus.OK,
                messageSource.getCoreMessageSource().getMessage(localeLang, null, LocaleContextHolder.getLocale())
            )
        )
}


