package com.tek.security.common.controller

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.core.swagger.ApiPageable
import com.tek.core.util.LoggerDelegate
import com.tek.security.common.TekSecurityPattern.PROFILE_PATH
import com.tek.security.common.model.RoleName
import com.tek.security.common.model.TekProfile
import com.tek.security.common.service.TekProfileService
import com.tek.security.common.util.hasRole
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
@Api(tags = ["Profiles"])
@RestController
@RequestMapping(path = [PROFILE_PATH], produces = [MediaType.APPLICATION_JSON_VALUE])
class TekProfileController(
    private val profileService: TekProfileService
) {

    private val log by LoggerDelegate()

    val readAuthorized get() = hasRole(RoleName.PROFILE_READ)

    @PreAuthorize("this.readAuthorized")
    @GetMapping("/list")
    @ApiPageable
    fun list(@ApiIgnore pageable: Pageable, @QuerydslPredicate predicate: Predicate?): ResponseEntity<TekPageResponse<TekProfile>> {
        log.debug("Executing [GET] method")
        return ResponseEntity(
            TekPageResponse(HttpStatus.OK, profileService.list(pageable, predicate)), HttpStatus.OK
        )
    }

    @PreAuthorize("this.readAuthorized")
    @GetMapping("/read/{id}")
    fun readById(@PathVariable("id") id: Long): ResponseEntity<TekResponseEntity<TekProfile>> {
        log.debug("Executing [GET] method")
        return ResponseEntity(
            TekResponseEntity(HttpStatus.OK, profileService.readOne(id)), HttpStatus.OK
        )
    }

    @PreAuthorize("this.readAuthorized")
    @GetMapping("/readByName/{name}")
    fun readByName(@PathVariable("name") name: String): ResponseEntity<TekResponseEntity<TekProfile>> {
        log.debug("Executing [GET] method")
        return ResponseEntity(
            TekResponseEntity(HttpStatus.OK, profileService.readOneByName(name)), HttpStatus.OK
        )
    }
}