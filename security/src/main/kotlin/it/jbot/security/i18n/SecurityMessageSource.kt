package it.jbot.security.i18n

import it.jbot.shared.i18n.JBotMessageSource
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource

@Configuration
class SecurityMessageSource : JBotMessageSource {
    
    override fun getResource(): MessageSource = getSecuritySource()
    
    @Bean
    fun getSecuritySource(): MessageSource =
        ReloadableResourceBundleMessageSource().apply {
            setBasename("classpath:/i18n/security_messages")
        }
    
    companion object {
        const val errorConflictUsername = "error.conflict.username"
        const val errorConflictEmail = "error.conflict.email"
        
        const val errorEmptyRole = "error.empty.role"
        
        const val errorRoleNotFound = "error.notfound.role"
    }
}