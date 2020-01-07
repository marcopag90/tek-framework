package com.tek.core.i18n

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource

@Configuration
class CoreMessageSource : TekMessageSource {

    override fun getResource(): MessageSource = getCoreMessageSource()

    @Bean
    fun getCoreMessageSource(): MessageSource =
        ReloadableResourceBundleMessageSource().apply {
            setBasename("classpath:/i18n/core_messages")
            setDefaultEncoding("UTF-8")
        }

    companion object {
        const val localeLang = "locale.lang"

        const val errorEmptyField = "error.empty.field"
        const val errorNotFoundResource = "error.notfound.resource"
        const val errorUnknownProperty = "error.unknown.property"

        const val messageInternalServerError = "message.internal.server.error"
    }

}