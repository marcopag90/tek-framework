package com.tek.security.controller

import com.tek.audit.javers.request.JaversQEntityParam
import com.tek.audit.javers.response.JaversCommitChanges
import com.tek.audit.service.JaversService
import com.tek.core.TekResponseEntity
import com.tek.core.util.LoggerDelegate
import com.tek.security.SecurityPattern.JAVERS_PATH
import com.tek.security.model.enums.PrivilegeName
import com.tek.security.util.hasPrivilege
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@Suppress("UNUSED")
@Api(tags = ["Javers Audit"])
@RestController
@RequestMapping(path = [JAVERS_PATH], produces = [MediaType.APPLICATION_JSON_VALUE])
class JaversController(
    @Qualifier(JaversService.security) private val javersService: JaversService
) {

    private val log by LoggerDelegate()

    val readAuthorized get() = hasPrivilege(PrivilegeName.AUDIT_READ)

    @PreAuthorize("this.readAuthorized")
    @GetMapping("/list/{entity}")
    fun listByEntity(
        @PathVariable("entity") entity: String,
        @RequestParam("skip", required = false) skip: Int?,
        @RequestParam("limit", required = false) limit: Int?,
        params: JaversQEntityParam
    ): ResponseEntity<TekResponseEntity<List<JaversCommitChanges>>> {
        log.debug("Executing [GET] method")
        return ResponseEntity.ok(
            TekResponseEntity(HttpStatus.OK, javersService.queryByEntity(entity, skip, limit, params))
        )
    }
}