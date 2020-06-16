package com.tek.audit.i18n

import com.tek.audit.AUDIT_MESSAGES
import com.tek.audit.AUDIT_MESSAGE_SOURCE
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource

@Configuration
class AuditMessageSource{

    @Bean(name = [AUDIT_MESSAGE_SOURCE])
    fun getAuditSource(): MessageSource =
        ReloadableResourceBundleMessageSource().apply {
            setBasenames(AUDIT_MESSAGES)
            setDefaultEncoding("UTF-8")
        }

    companion object {

        const val javersPropertyAdded = "javers.property.added"
        const val javersPropertyRemoved = "javers.property.removed"
        const val javersPropertyValueChanged = "javers.property.value.changed"

        const val javersEntityInserted = "javers.entity.inserted"
        const val javersEntityRemoved = "javers.entity.removed"
    }
}