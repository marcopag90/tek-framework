package com.tek.core.i18n.session

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Suppress("unused")
@Configuration
@ConditionalOnBean(TekSessionLocale::class)
class TekSessionLocaleConf(
    private val locale: TekSessionLocale
) : WebMvcConfigurer {

    companion object {
        private val log = LoggerFactory.getLogger(TekSessionLocaleConf::class.java)
    }

    /**
     * Add an handler interceptor for [TekSessionLocale] changes.
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        log.info("Registering locale resolver: ${locale.localeResolver()}")
        registry.addInterceptor(locale.localChangeInterceptor())
    }
}


