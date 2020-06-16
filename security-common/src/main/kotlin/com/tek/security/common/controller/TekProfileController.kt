package com.tek.security.common.controller

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.swagger.ApiPageable
import com.tek.security.common.PROFILE_PATH
import com.tek.security.common.crud.TekProfileCrudService
import com.tek.security.common.form.ProfileCreateForm
import com.tek.security.common.form.ProfileUpdateForm
import com.tek.security.common.model.TekProfile
import io.swagger.annotations.Api
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@Api(tags = ["Profiles"])
@RestController
@RequestMapping(path = [PROFILE_PATH], produces = [MediaType.APPLICATION_JSON_VALUE])
class TekProfileController :
    TekAuthWritableController<TekProfile, Long, TekProfileCrudService, ProfileCreateForm, ProfileUpdateForm>() {

    @Suppress("RedundantOverride")
    @ApiPageable
    override fun list(
        pageable: Pageable,
        @QuerydslPredicate predicate: Predicate?
    ): ResponseEntity<TekPageResponse<TekProfile>> {
        return super.list(pageable, predicate)
    }
}