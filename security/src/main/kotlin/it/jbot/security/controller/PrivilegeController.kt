package it.jbot.security.controller

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotPageResponse
import it.jbot.core.JBotResponseEntity
import it.jbot.core.util.LoggerDelegate
import it.jbot.security.model.Privilege
import it.jbot.security.model.Role
import it.jbot.security.model.enums.PrivilegeName
import it.jbot.security.service.PrivilegeService
import it.jbot.security.util.hasPrivilege
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/privilege")
class PrivilegeController(
    private val service: PrivilegeService
) {

    private val log by LoggerDelegate()

    fun readAuthorized() = hasPrivilege(PrivilegeName.ROLE_READ)

    @PreAuthorize("this.readAuthorized()")
    @GetMapping("/list")
    fun list(pageable: Pageable, @QuerydslPredicate predicate: Predicate?): ResponseEntity<JBotPageResponse<Privilege>> {
        log.debug("Executing [GET] method")
        return service.list(pageable, predicate)
    }

    @PreAuthorize("this.readAuthorized()")
    @GetMapping("/read")
    fun read(@RequestParam("name") name: String): ResponseEntity<JBotResponseEntity<Privilege>> =
        service.read(name)

}