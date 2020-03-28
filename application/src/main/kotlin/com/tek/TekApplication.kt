package com.tek

import com.tek.core.TEK_PACKAGES_TO_SCAN
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication(scanBasePackages = [TEK_PACKAGES_TO_SCAN])
@EnableJpaAuditing
class TekApplication

fun main(args: Array<String>) {
    runApplication<TekApplication>(*args)
}
