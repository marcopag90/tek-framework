package it.jbot.security.xml

import it.jbot.security.xml.model.Privileges
import it.jbot.security.xml.model.Roles
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.xml.bind.JAXBContext
import javax.xml.bind.Unmarshaller

@Configuration
class JAXBAuthConfiguration {

    @Bean
    fun getRoleUnmarshaller(): Unmarshaller {
        val context = JAXBContext.newInstance(Roles::class.java)
        return context.createUnmarshaller()
    }

    @Bean
    fun getPrivilgesUnmarshaller(): Unmarshaller {
        val context = JAXBContext.newInstance(Privileges::class.java)
        return context.createUnmarshaller()
    }
}