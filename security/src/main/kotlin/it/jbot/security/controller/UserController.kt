package it.jbot.security.controller

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotPageEntityResponse
import it.jbot.core.web.CrudController
import it.jbot.security.model.User
import it.jbot.security.repository.UserRepository
import it.jbot.security.service.UserService
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    override val service: UserService<User>,
    private val repository: UserRepository
) : CrudController<User>(service) {

    override fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageEntityResponse<User>> {

        predicate?.let {
            return ResponseEntity(
                JBotPageEntityResponse(HttpStatus.OK, repository.findAll(predicate, pageable)),
                HttpStatus.OK
            )
        } ?: return ResponseEntity(JBotPageEntityResponse(HttpStatus.OK, repository.findAll(pageable)), HttpStatus.OK)
    }
}