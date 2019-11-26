package it.jbot.security.adapter

import it.jbot.core.AbstractJBotAdapter
import it.jbot.core.JBotResponse
import it.jbot.security.model.User
import it.jbot.security.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component("UserAdapter")
class UserAdapter(
    private val repo: UserRepository
) : AbstractJBotAdapter<User>() {

    override fun createEntity(properties: Map<String, Any?>): ResponseEntity<JBotResponse> {

        return ResponseEntity(
            JBotResponse(
                HttpStatus.OK, null
            ),
            HttpStatus.OK
        )
    }
}