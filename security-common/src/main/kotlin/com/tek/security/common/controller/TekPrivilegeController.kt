package com.tek.security.common.controller

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.core.swagger.ApiPageable
import com.tek.core.util.LoggerDelegate
import com.tek.security.common.TekSecurityPattern.PRIVILEGE_PATH
import com.tek.security.common.model.TekPrivilege
import com.tek.security.common.model.enums.PrivilegeName
import com.tek.security.common.service.TekPrivilegeService
import com.tek.security.common.util.hasPrivilege
import io.swagger.annotations.Api
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore

@Suppress("unused")
@Api(tags = ["Privileges"])
@RestController
@RequestMapping(path = [PRIVILEGE_PATH], produces = [MediaType.APPLICATION_JSON_VALUE])
class TekPrivilegeController(
    private val tekPrivilegeService: TekPrivilegeService
) {

    private val log by LoggerDelegate()

    val readAuthorized get() = hasPrivilege(PrivilegeName.ROLE_READ)

    @PreAuthorize("this.readAuthorized")
    @GetMapping("/list")
    @ApiPageable
    fun list(@ApiIgnore pageable: Pageable, @QuerydslPredicate predicate: Predicate?): ResponseEntity<TekPageResponse<TekPrivilege>> {
        log.debug("Executing [GET] method")
        return ResponseEntity.ok(
            TekPageResponse(HttpStatus.OK, tekPrivilegeService.list(pageable, predicate))
        )
    }

    @PreAuthorize("this.readAuthorized")
    @GetMapping("/read/{id}")
    fun read(@PathVariable("id") id: Long): ResponseEntity<TekResponseEntity<TekPrivilege>> {
        log.debug("Executing [GET] method")
        return ResponseEntity.ok(
            TekResponseEntity(HttpStatus.OK, tekPrivilegeService.readOne(id))
        )
    }
}