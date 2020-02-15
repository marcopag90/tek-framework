package com.tek.core.i18n.cookie

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Suppress("unused")
@Configuration
@ConditionalOnBean(TekCookieLocale::class)
class TekCookieLocaleConf(
    private val locale: TekCookieLocale
) : WebMvcConfigurer {

    companion object {
        private val log = LoggerFactory.getLogger(TekCookieLocaleConf::class.java)
    }

    /**
     * Add an handler interceptor for [TekCookieLocale] changes.
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        log.info("Registering locale resolver: ${locale.localeResolver()}")
        registry.addInterceptor(locale.localChangeInterceptor())
    }
}


