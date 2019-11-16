package it.jbot.shared.controller

import it.jbot.shared.i18n.SharedMessageSource
import it.jbot.shared.i18n.SharedMessageSource.Companion.localeLang
import it.jbot.shared.web.JBotResponse
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
    private val messageSource: SharedMessageSource
) {
    
    @GetMapping
    fun getCurrentLocale(): ResponseEntity<JBotResponse> =
        ResponseEntity<JBotResponse>(
            JBotResponse(HttpStatus.OK).apply {
                result = messageSource.getSharedSource().getMessage(
                    localeLang,
                    null,
                    LocaleContextHolder.getLocale()
                )
            },
            HttpStatus.OK
        )
}