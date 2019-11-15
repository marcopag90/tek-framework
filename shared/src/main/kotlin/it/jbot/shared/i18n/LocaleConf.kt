package it.jbot.shared.i18n

import it.jbot.shared.i18n.JBotMessage.localLanguage
import it.jbot.shared.web.JBotResponse
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*

const val LOCALE_PATTERN = "/locale"

@RestController
@RequestMapping(LOCALE_PATTERN)
class LocaleController(
    private val messageSource: MessageSource
) {
    
    @GetMapping
    fun getCurrentLocale(): ResponseEntity<JBotResponse> =
        ResponseEntity<JBotResponse>(
            JBotResponse(HttpStatus.OK).apply {
                result = messageSource.getMessage(
                    localLanguage,
                    null,
                    LocaleContextHolder.getLocale()
                )
            },
            HttpStatus.OK
        )
}

@Component
class JBotLocale {
    
    /**
     * Bean to determine current _locale_ (session, cookie, accept-language header based).
     *
     * Provides a default one if no one can be determined.
     */
    @Bean
    fun localeResolver(): LocaleResolver = SessionLocaleResolver().apply {
        setDefaultLocale(Locale.ITALY)
    }
    
    /**
     * Interceptor that will switch to a new _locale_ based on the value of the _lang_ parameter
     * appended inside a Url request as a [org.springframework.web.bind.annotation.RequestParam]
     */
    @Bean
    fun localChangeInterceptor() = LocaleChangeInterceptor().apply {
        paramName = ("lang")
    }
}