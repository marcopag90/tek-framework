package com.tek.security.common.controller

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.security.common.TekSecurityPattern.PROFILE_PATH
import com.tek.security.common.form.ProfileForm
import com.tek.security.common.model.RoleName
import com.tek.security.common.model.TekProfile
import com.tek.security.common.service.provider.TekProfileServiceProvider
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

@Suppress("unused")
@Api(tags = ["Profiles"])
@RestController
@RequestMapping(path = [PROFILE_PATH], produces = [MediaType.APPLICATION_JSON_VALUE])
class TekProfileController :
    TekAuthorizedCrudEntityController<TekProfile, Long, TekProfileServiceProvider, ProfileForm>() {

    override fun list(
        pageable: Pageable,
        @QuerydslPredicate predicate: Predicate?
    ): ResponseEntity<TekPageResponse<TekProfile>> = super.list(pageable, predicate)

    @GetMapping("/readByName/{name}")
    @PreAuthorize("this.readAuthorized")
    fun readOneByName(
        @PathVariable("name") name: String
    ): ResponseEntity<TekResponseEntity<TekProfile>> {
        return ResponseEntity.ok(
            TekResponseEntity(HttpStatus.OK, service.readOneByName(name))
        )
    }

    override val createAuthorized get() = hasRole(RoleName.PROFILE_CREATE)
    override val readAuthorized get() = hasRole(RoleName.PROFILE_READ)
    override val updateAuthorized get() = hasRole(RoleName.PROFILE_UPDATE)
    override val deleteAuthorized get() = hasRole(RoleName.PROFILE_DELETE)

}