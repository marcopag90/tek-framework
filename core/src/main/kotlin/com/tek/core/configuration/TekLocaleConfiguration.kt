package com.tek.core.configuration

import com.tek.core.TekCoreProperties
import com.tek.core.TekLocaleProperties.TekLocaleType
import com.tek.core.util.LoggerDelegate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*

/**
 * Configuration to determine:
 *
 * - [LocaleResolver]: see [TekLocaleType]
 *
 * - [LocaleChangeInterceptor]: trigger parameter to detect the locale change
 */
@Configuration
class TekLocaleConfiguration(
    coreProperties: TekCoreProperties
) : WebMvcConfigurer {

    companion object {
        private val log by LoggerDelegate()
    }

    private val props = coreProperties.locale

    /**
     * Add a handler interceptor for [LocaleResolver].
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        log.info("Registering locale resolver: {}", localeResolver())
        registry.addInterceptor(localeChangeInterceptor())
    }

    @Bean
    fun localeResolver(): LocaleResolver {
        return when (props.type) {
            TekLocaleType.SESSION -> SessionLocaleResolver().apply {
                setDefaultLocale(Locale.ENGLISH)
            }
            TekLocaleType.COOKIE -> CookieLocaleResolver().apply {
                setDefaultLocale(Locale.ENGLISH)
                cookieName = props.cookieName
                cookieMaxAge = props.cookieMaxAge
            }
            else -> throw NotImplementedError()
        }
    }

    /**
     * Interceptor that will switch to a new _locale_ based on the value of the _lang_ parameter
     * appended inside the Url request as a [org.springframework.web.bind.annotation.RequestParam]
     */
    @Bean
    fun localeChangeInterceptor() = LocaleChangeInterceptor().apply {
        paramName = props.interceptorName
    }
}