package it.jbot.security.adapter

import com.querydsl.core.types.Predicate
import it.jbot.core.DalService
import it.jbot.core.JBotResponse
import it.jbot.security.model.User
import it.jbot.security.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap

@Component("UserDalService")
class UserDalService(
    private val repo: UserRepository
) : DalService<User>() {

    //TODO decide if an user can be manually created
    override fun create(properties: Map<String, Any?>): ResponseEntity<JBotResponse> {
        return ResponseEntity(
            JBotResponse(
                null
            ),
            HttpStatus.OK
        )
    }

    override fun list(properties: MultiValueMap<String, String>, pageable: Pageable): ResponseEntity<JBotResponse> {

        var predicate: Predicate? = null

        return ResponseEntity(
            JBotResponse(
                repo.findAll(pageable)
            ),
            HttpStatus.OK
        )
    }
}