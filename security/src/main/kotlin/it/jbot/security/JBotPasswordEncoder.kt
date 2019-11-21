package it.jbot.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

/**
 * Default JBot Password Encoder {bcript}
 */
@Component
class JBotPasswordEncoder {
    fun encoder() = BCryptPasswordEncoder()
}
