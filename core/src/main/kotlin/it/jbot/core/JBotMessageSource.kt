package it.jbot.core

import org.springframework.context.MessageSource

interface JBotMessageSource {
    fun getResource() : MessageSource
}