package com.tek.security.common.controller

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.core.swagger.ApiPageable
import com.tek.core.util.LoggerDelegate
import com.tek.security.common.SecurityPattern.USER_PATH
import com.tek.security.common.model.TekUser
import com.tek.security.common.model.enums.PrivilegeName
import com.tek.security.common.service.TekUserService
import com.tek.security.common.util.hasPrivilege
import io.swagger.annotations.Api
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@Suppress("UNUSED")
@Api(tags = ["Users"])
@RestController
@RequestMapping(path = [USER_PATH], produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController(
    private val tekUserService: TekUserService
) {

    private val log by LoggerDelegate()

    val createAuthorized get() = hasPrivilege(PrivilegeName.USER_CREATE)

    val readAuthorized get() = hasPrivilege(PrivilegeName.USER_READ)

    val updateAuthorized get() = hasPrivilege(PrivilegeName.USER_UPDATE)

    val deleteAuthorized get() = hasPrivilege(PrivilegeName.USER_DELETE)

    @PreAuthorize("this.readAuthorized")
    @GetMapping("/list")
    @ApiPageable
    fun list(@ApiIgnore pageable: Pageable, @QuerydslPredicate predicate: Predicate?): ResponseEntity<TekPageResponse<TekUser>> {
        log.debug("Executing [GET] method")
        return ResponseEntity.ok(
            TekPageResponse(HttpStatus.OK, tekUserService.list(pageable, predicate))
        )
    }

    @PreAuthorize("this.readAuthorized")
    @GetMapping("/read/{id}")
    fun read(@PathVariable("id") id: Long): ResponseEntity<TekResponseEntity<TekUser>> {
        log.debug("Executing [GET] method")
        return ResponseEntity.ok(
            TekResponseEntity(HttpStatus.OK, tekUserService.readOne(id))
        )
    }

    @PreAuthorize("this.updateAuthorized")
    @PatchMapping("/update/{id}")
    fun update(@RequestBody properties: Map<String, Any?>, @PathVariable("id") id: Long): ResponseEntity<TekResponseEntity<TekUser>> {
        log.debug("Executing [PATCH] method")
        return ResponseEntity.ok(
            TekResponseEntity(HttpStatus.OK, tekUserService.update(properties, id))
        )
    }

    @PreAuthorize("this.deleteAuthorized")
    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<TekResponseEntity<Long>> {
        log.debug("Executing [DELETE] method")
        return ResponseEntity.ok(
            TekResponseEntity(HttpStatus.OK, tekUserService.delete(id))
        )
    }
}


