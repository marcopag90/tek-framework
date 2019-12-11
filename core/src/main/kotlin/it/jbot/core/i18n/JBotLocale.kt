package it.jbot.core.i18n

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*

/**
 * Bean to determine current _locale_ (session, cookie, accept-language header based).
 *
 * Provides a default one if no one can be determined.
 */
@Component
class JBotLocale {

    @Bean
    fun localeResolver(): LocaleResolver = SessionLocaleResolver().apply {
        setDefaultLocale(Locale.ENGLISH)
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