package it.jbot.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class JBotPasswordEncoder {
    
    fun encoder() = BCryptPasswordEncoder()
}
