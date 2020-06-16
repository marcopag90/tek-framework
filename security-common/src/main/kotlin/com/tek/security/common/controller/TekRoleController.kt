package com.tek.security.common.controller

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.swagger.ApiPageable
import com.tek.security.common.ROLE_PATH
import com.tek.security.common.crud.TekRoleCrudService
import com.tek.security.common.model.TekRole
import io.swagger.annotations.Api
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@Api(tags = ["Roles"])
@RestController
@RequestMapping(path = [ROLE_PATH], produces = [MediaType.APPLICATION_JSON_VALUE])
class TekRoleController : TekAuthReadOnlyController<TekRole, Long, TekRoleCrudService>() {

    @Suppress("RedundantOverride")
    @ApiPageable
    override fun list(
        pageable: Pageable, @QuerydslPredicate predicate: Predicate?
    ): ResponseEntity<TekPageResponse<TekRole>> {
        return super.list(pageable, predicate)
    }
}

