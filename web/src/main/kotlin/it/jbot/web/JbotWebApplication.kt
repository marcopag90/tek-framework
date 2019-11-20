package it.jbot.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JbotWebApplication

fun main(args: Array<String>) {
	runApplication<JbotWebApplication>(*args)
}
