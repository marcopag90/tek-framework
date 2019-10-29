package it.jbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer


@SpringBootApplication(scanBasePackages = ["it.jbot"])
@EnableAuthorizationServer
@EnableResourceServer
@EnableJpaAuditing
class JBotApplication

fun main(args: Array<String>) {
    runApplication<JBotApplication>(*args)
}
