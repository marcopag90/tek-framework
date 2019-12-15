package it.jbot.security.controller

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotEntityResponse
import it.jbot.core.JBotPageResponse
import it.jbot.core.controller.CrudController
import it.jbot.security.model.JBotUser
import it.jbot.security.service.UserService
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    override val service: UserService
) : CrudController<JBotUser, Long>(service) {

    @PreAuthorize("hasAuthority('USER_READ')")
    override fun listResolve(
        pageable: Pageable, @QuerydslPredicate predicate: Predicate?
    ): ResponseEntity<JBotPageResponse<JBotUser>> = list(pageable, predicate)

    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @PatchMapping("/update/{id}")
    override fun update(properties: Map<String, Any?>, @PathVariable("id") id: Long): ResponseEntity<JBotEntityResponse<JBotUser>> {
        return super.update(properties, id)
    }

}
