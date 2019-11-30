package it.jbot.core.i18n

import it.jbot.core.JBotMessageSource
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource

@Configuration
class CoreMessageSource : JBotMessageSource {
    
    override fun getResource(): MessageSource = getWebResource()
    
    @Bean
    fun getWebResource(): MessageSource =
        ReloadableResourceBundleMessageSource().apply {
            setBasename("classpath:/i18n/core_messages")
        }
    
    companion object {
        const val localeLang = "locale.lang"
    }
    
}