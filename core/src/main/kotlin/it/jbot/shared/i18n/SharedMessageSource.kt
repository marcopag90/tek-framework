package it.jbot.shared.i18n

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource

@Configuration
class SharedMessageSource : JBotMessageSource {
    
    override fun getResource(): MessageSource = getSharedSource()
    
    @Bean
    fun getSharedSource(): MessageSource =
        ReloadableResourceBundleMessageSource().apply {
            setBasename("classpath:/i18n/shared_messages")
        }
    
    companion object {
        const val localeLang = "locale.lang"
    }
    
}