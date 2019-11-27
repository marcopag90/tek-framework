package it.jbot.security.adapter

import it.jbot.core.DalService
import it.jbot.core.JBotResponse
import it.jbot.security.model.User
import it.jbot.security.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component("UserDalService")
class UserDalService(
    private val repo: UserRepository
) : DalService<User>() {

    override fun createEntity(properties: Map<String, Any?>): ResponseEntity<JBotResponse> {

        return ResponseEntity(
            JBotResponse(
                HttpStatus.OK, null
            ),
            HttpStatus.OK
        )
    }
}