package com.tek.security.controller

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.core.swagger.ApiPageable
import com.tek.core.util.LoggerDelegate
import com.tek.security.model.Privilege
import com.tek.security.model.enums.PrivilegeName
import com.tek.security.service.PrivilegeService
import com.tek.security.util.hasPrivilege
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping("/privilege")
class PrivilegeController(
    private val service: PrivilegeService
) {

    private val log by LoggerDelegate()

    fun readAuthorized() = hasPrivilege(PrivilegeName.ROLE_READ)

    @PreAuthorize("this.readAuthorized()")
    @GetMapping("/list")
    @ApiPageable
    fun list(@ApiIgnore pageable: Pageable, @QuerydslPredicate predicate: Predicate?): ResponseEntity<TekPageResponse<Privilege>> {
        log.debug("Executing [GET] method")
        return service.list(pageable, predicate)
    }

    @PreAuthorize("this.readAuthorized()")
    @GetMapping("/read")
    fun read(@RequestParam("name") name: String): ResponseEntity<TekResponseEntity<Privilege>> =
        service.read(name)
}