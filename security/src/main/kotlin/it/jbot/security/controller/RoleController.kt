package it.jbot.security.controller

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotBaseResponse
import it.jbot.core.JBotPageResponse
import it.jbot.core.controller.JBotCrudController
import it.jbot.security.model.Role
import it.jbot.security.service.impl.RoleCrudService
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
class RoleController(
    service: RoleCrudService
) : JBotCrudController<Role, Long, RoleCrudService>(service) {

}