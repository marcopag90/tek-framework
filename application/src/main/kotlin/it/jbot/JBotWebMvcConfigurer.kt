package it.jbot

import it.jbot.shared.i18n.JBotLocale
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class JBotWebMvcConfigurer(
    private val locale: JBotLocale
) : WebMvcConfigurer {
    
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(locale.localChangeInterceptor())
    }
    
}