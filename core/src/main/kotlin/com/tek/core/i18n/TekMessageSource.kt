package com.tek.core.i18n

import org.springframework.context.MessageSource

interface TekMessageSource {
    fun getResource() : MessageSource
}