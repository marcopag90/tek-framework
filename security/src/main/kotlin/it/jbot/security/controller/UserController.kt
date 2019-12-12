package it.jbot.security.controller

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotPageResponse
import it.jbot.core.controller.CrudController
import it.jbot.security.model.User
import it.jbot.security.service.UserService
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    override val service: UserService
) : CrudController<User, Long>(service) {

    override fun listResolve(
        pageable: Pageable, @QuerydslPredicate predicate: Predicate?
    ): ResponseEntity<JBotPageResponse<User>> = list(pageable, predicate)

}
