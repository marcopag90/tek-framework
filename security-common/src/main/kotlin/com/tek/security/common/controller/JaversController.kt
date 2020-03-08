package com.tek.security.common.controller

import com.tek.audit.javers.JaversEntityChanges
import com.tek.audit.javers.JaversEntityListChanges
import com.tek.audit.javers.JaversQEntityParam
import com.tek.audit.javers.JaversQPage
import com.tek.audit.service.JaversQService
import com.tek.core.TekResponseEntity
import com.tek.core.util.LoggerDelegate
import com.tek.security.common.TekSecurityPattern.JAVERS_PATH
import com.tek.security.common.model.enums.PrivilegeName
import com.tek.security.common.util.hasPrivilege
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@Suppress("unused")
@Api(tags = ["Entity Audit"])
@RestController
@RequestMapping(path = [JAVERS_PATH], produces = [MediaType.APPLICATION_JSON_VALUE])
class JaversController(
    private val javersService: JaversQService
) {

    private val log by LoggerDelegate()

    val readAuthorized get() = hasPrivilege(PrivilegeName.AUDIT_READ)

    @PreAuthorize("this.readAuthorized")
    @GetMapping("/list/entities")
    fun getAuditableEntities(): ResponseEntity<TekResponseEntity<List<String>>> {
        log.debug("Executing [GET] method")
        return ResponseEntity.ok(
            TekResponseEntity(HttpStatus.OK, javersService.getAuditableEntities())
        )
    }

    @PreAuthorize("this.readAuthorized")
    @GetMapping("/list/{entity}")
    fun listByEntity(
        @PathVariable("entity") entity: String,
        @RequestParam("skip", required = false) skip: Int?,
        @RequestParam("limit", required = false) limit: Int?,
        params: JaversQEntityParam
    ): ResponseEntity<TekResponseEntity<List<JaversEntityListChanges>>> {
        log.debug("Executing [GET] method")
        return ResponseEntity.ok(
            TekResponseEntity(
                HttpStatus.OK,
                javersService.queryChangesByEntity(entity, JaversQPage(skip, limit), params)
            )
        )
    }

    @PreAuthorize("this.readAuthorized")
    @GetMapping("/read/{entity}/{id}")
    fun readByCommit(
        @PathVariable("entity") entity: String,
        @PathVariable("id") id: BigDecimal
    ): ResponseEntity<TekResponseEntity<List<JaversEntityChanges>>> {
        log.debug("Executing [GET] method")
        return ResponseEntity.ok(
            TekResponseEntity(HttpStatus.OK, javersService.queryChangesByCommit(entity, id))
        )
    }
}