package com.tek

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication(scanBasePackages = ["com.tek"])
@EnableJpaAuditing
class TekApplication

fun main(args: Array<String>) {
    runApplication<TekApplication>(*args)
}
