package com.tek.security.common.controller

import com.querydsl.core.types.Predicate
import com.tek.core.TekBaseResponse
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.core.controller.TekCrudEntityController
import com.tek.core.form.AbstractForm
import com.tek.core.service.ICrudEntityService
import com.tek.core.swagger.ApiPageable
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import javax.validation.Valid

/**
 * Extension of [TekCrudEntityController] to provide security at method access level
 */
@Suppress("unused")
abstract class TekAuthorizedCrudEntityController<E, ID, S : ICrudEntityService<E, ID, DTO>, DTO : AbstractForm>(
) : TekCrudEntityController<E, ID, S, DTO>() {

    @PreAuthorize("this.createAuthorized")
    @GetMapping("/list")
    @ApiPageable
    override fun list(@ApiIgnore pageable: Pageable, predicate: Predicate?): ResponseEntity<TekPageResponse<E>> {
        return super.list(pageable, predicate)
    }

    @PreAuthorize("this.readAuthorized")
    @GetMapping("/read/{id}")
    override fun read(id: ID): ResponseEntity<TekResponseEntity<E>> {
        return super.read(id)
    }

    @PreAuthorize("this.updateAuthorized")
    @PatchMapping("/update/{id}")
    override fun update(@RequestBody properties: Map<String, Any?>, @PathVariable("id") id: ID): ResponseEntity<TekResponseEntity<E>> {
        return super.update(properties, id)
    }

    @PreAuthorize("this.updateAuthorized")
    @PostMapping("/update/{id}")
    override fun update(@RequestBody @Valid form: DTO, @PathVariable("id") id: ID): ResponseEntity<TekResponseEntity<E>> {
        return super.update(form, id)
    }

    @PreAuthorize("this.deleteAuthorized")
    @DeleteMapping("/delete/{id}")
    override fun delete(@PathVariable("id") id: ID): ResponseEntity<TekBaseResponse> {
        return super.delete(id)
    }

    /**
     * Check if the user in session has a [org.springframework.security.core.GrantedAuthority] for the _CREATE_ method
     */
    abstract val createAuthorized: Boolean

    /**
     * Check if the user in session has a [org.springframework.security.core.GrantedAuthority] for the _READ_ method
     */
    abstract val readAuthorized: Boolean

    /**
     * Check if the user in session has a [org.springframework.security.core.GrantedAuthority] for the _UPDATE_ method
     */
    abstract val updateAuthorized: Boolean

    /**
     * Check if the user in session has a [org.springframework.security.core.GrantedAuthority] for the _DELETE_ method
     */
    abstract val deleteAuthorized: Boolean
}