package it.jbot.web.i18n

import org.springframework.context.MessageSource

interface JBotMessageSource {
    fun getResource() : MessageSource
}