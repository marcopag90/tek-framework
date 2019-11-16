package it.jbot.shared.i18n

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource

@Configuration
class SharedMessageSource {
    
    @Bean
    fun getSharedSource(): MessageSource =
        ReloadableResourceBundleMessageSource().apply {
            setBasename("classpath:/shared_messages")
        }
}