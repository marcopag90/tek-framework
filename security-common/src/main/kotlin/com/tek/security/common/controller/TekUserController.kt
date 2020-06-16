package com.tek.security.common.controller

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.core.controller.UPDATE_MAPPING
import com.tek.core.swagger.ApiPageable
import com.tek.security.common.USER_PATH
import com.tek.security.common.crud.TekUserCrudService
import com.tek.security.common.form.UserCreateForm
import com.tek.security.common.form.UserUpdateForm
import com.tek.security.common.model.TekUser
import io.swagger.annotations.Api
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@Suppress("unused")
@Api(tags = ["Users"])
@RestController
@RequestMapping(path = [USER_PATH], produces = [MediaType.APPLICATION_JSON_VALUE])
class TekUserController :
    TekAuthWritableController<TekUser, Long, TekUserCrudService, UserCreateForm, UserUpdateForm>() {

    @Suppress("RedundantOverride")
    @ApiPageable
    override fun list(
        pageable: Pageable, @QuerydslPredicate predicate: Predicate?
    ): ResponseEntity<TekPageResponse<TekUser>> {
        return super.list(pageable, predicate)
    }

    @PreAuthorize("this.updateAuthorized")
    @PatchMapping(UPDATE_MAPPING)
    fun update(
        @RequestBody properties: MutableMap<String, Any?>,
        @PathVariable("id") id: Long
    ): ResponseEntity<TekResponseEntity<TekUser>> {
        return ResponseEntity.ok(TekResponseEntity(HttpStatus.OK, service.update(properties, id)))
    }
}