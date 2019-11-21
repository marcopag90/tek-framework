package it.jbot.web.controller

import it.jbot.web.i18n.WebMessageSource
import it.jbot.web.i18n.WebMessageSource.Companion.localeLang
import it.jbot.web.JBotResponse
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
    private val messageSource: WebMessageSource
) {
    
    @GetMapping
    fun getCurrentLocale(): ResponseEntity<JBotResponse> =
        ResponseEntity(
            JBotResponse(HttpStatus.OK).apply {
                this.result = messageSource.getWebResource().getMessage(
                    localeLang,
                    null,
                    LocaleContextHolder.getLocale()
                )
            },
            HttpStatus.OK
        )
}