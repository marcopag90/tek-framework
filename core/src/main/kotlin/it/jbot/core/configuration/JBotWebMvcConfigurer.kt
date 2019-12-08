package it.jbot.core.configuration

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module
import it.jbot.core.component.JBotLocale
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class JBotWebMvcConfigurer(
    private val locale: JBotLocale
) : WebMvcConfigurer {

    /**
     * Add an handler interceptor for [JBotLocale] changes.
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(locale.localChangeInterceptor())
    }

    /**
     * Add an handler interceptor to avoid [MappingJackson2HttpMessageConverter] serialization failure
     * on lazy proxy objects retrieved from Hibernate, when no session is in context.
     */
    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        for (converter in converters) {
            if (converter is MappingJackson2HttpMessageConverter) {
                val mapper = converter.objectMapper
                mapper.registerModule(Hibernate5Module())
            }
        }
    }
}


