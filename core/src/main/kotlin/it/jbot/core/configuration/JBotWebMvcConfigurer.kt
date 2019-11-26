package it.jbot.core.configuration

import it.jbot.core.component.JBotLocale
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


