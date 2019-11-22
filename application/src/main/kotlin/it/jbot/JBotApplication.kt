package it.jbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication(scanBasePackages = ["it.jbot"])

@EnableJpaAuditing
class JBotApplication

fun main(args: Array<String>) {
    runApplication<JBotApplication>(*args)
}
