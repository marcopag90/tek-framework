package it.jbot.security.i18n

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource

@Configuration
class SecurityMessageBundle {
    
    @Bean
    fun getSecuritySource(): MessageSource =
        ReloadableResourceBundleMessageSource().apply {
            setBasename("classpath:/i18n/security_messages")
        }
    
    companion object {
        const val errorConflictUsername = "error.conflict.username"
    }
}