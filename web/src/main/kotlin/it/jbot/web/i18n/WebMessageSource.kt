package it.jbot.web.i18n

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource

@Configuration
class WebMessageSource : JBotMessageSource {
    
    override fun getResource(): MessageSource = getWebResource()
    
    @Bean
    fun getWebResource(): MessageSource =
        ReloadableResourceBundleMessageSource().apply {
            setBasename("classpath:/i18n/web_messages")
        }
    
    companion object {
        const val localeLang = "locale.lang"
    }
    
}