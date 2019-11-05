package it.jbot

import it.jbot.security.oauth.configuration.ClientDetailsProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication(scanBasePackages = ["it.jbot"])

@EnableJpaAuditing
class JBotApplication

fun main(args: Array<String>) {
    runApplication<JBotApplication>(*args)
}
