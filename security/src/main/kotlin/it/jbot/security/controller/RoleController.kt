package it.jbot.security.controller

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotBaseResponse
import it.jbot.core.JBotPageResponse
import it.jbot.core.controller.CrudController
import it.jbot.security.model.Role
import it.jbot.security.service.RoleService
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/role")
@PreAuthorize("hasRole('ROLE_ADMIN')")
class RoleController(
    override val service: RoleService
) : CrudController<Role, Long>(service) {

    @GetMapping
    fun read(@RequestParam("name") name: String): ResponseEntity<JBotBaseResponse> =
        service.read(name)

    override fun listResolve(pageable: Pageable, @QuerydslPredicate predicate: Predicate?): ResponseEntity<JBotPageResponse<Role>> =
        super.list(pageable, predicate)

}