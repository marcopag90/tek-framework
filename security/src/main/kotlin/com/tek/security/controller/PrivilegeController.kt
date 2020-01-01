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
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@Suppress("UNUSED")
@RestController
@RequestMapping(path = ["/privilege"], produces = [MediaType.APPLICATION_JSON_VALUE])
class PrivilegeController(
    private val privilegeService: PrivilegeService
) {

    private val log by LoggerDelegate()

    val readAuthorized get() = hasPrivilege(PrivilegeName.ROLE_READ)

    @PreAuthorize("this.readAuthorized")
    @GetMapping("/list")
    @ApiPageable
    fun list(@ApiIgnore pageable: Pageable, @QuerydslPredicate predicate: Predicate?): ResponseEntity<TekPageResponse<Privilege>> {
        log.debug("Executing [GET] method")
        return ResponseEntity.ok(
            TekPageResponse(HttpStatus.OK, privilegeService.list(pageable, predicate))
        )
    }

    @PreAuthorize("this.readAuthorized")
    @GetMapping("/read/{id}")
    fun read(@PathVariable("id") id: Long): ResponseEntity<TekResponseEntity<Privilege>> {
        log.debug("Executing [GET] method")
        return ResponseEntity.ok(
            TekResponseEntity(HttpStatus.OK, privilegeService.readOne(id))
        )
    }
}