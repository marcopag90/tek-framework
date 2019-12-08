package it.jbot.security.controller

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotResponse
import it.jbot.security.model.User
import it.jbot.security.repository.UserRepository
import it.jbot.security.service.UserService
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    val repo: UserRepository,
    val service: UserService
) {

    @GetMapping("list")
    fun list(
        pageable: Pageable,
        @QuerydslPredicate(root = User::class) predicate: Predicate?
    ): ResponseEntity<JBotResponse> {

        predicate?.let {
            return ResponseEntity(JBotResponse(HttpStatus.OK, repo.findAll(predicate, pageable)), HttpStatus.OK)
        } ?: return ResponseEntity(JBotResponse(HttpStatus.OK, repo.findAll(pageable)), HttpStatus.OK)
    }

}