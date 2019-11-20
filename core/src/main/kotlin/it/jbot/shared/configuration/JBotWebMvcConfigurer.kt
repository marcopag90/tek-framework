package it.jbot.shared.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*

@Configuration
class JBotWebMvcConfigurer(
    private val locale: JBotLocale
) : WebMvcConfigurer {
    
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(locale.localChangeInterceptor())
    }
    
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