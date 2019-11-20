package it.jbot.shared.i18n

import org.springframework.context.MessageSource

interface JBotMessageSource {
    fun getResource() : MessageSource
}