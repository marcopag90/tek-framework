package com.tek.security.controller

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.core.controller.TekCrudController
import com.tek.core.form.AbstractForm
import com.tek.core.service.ICrudService
import com.tek.core.swagger.ApiPageable
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import javax.validation.Valid

/**
 * Extension of [TekCrudController] to provide security at method access level
 */
@Suppress("UNUSED")
abstract class TekAuthorizedCrudController<Entity, ID, Service : ICrudService<Entity, ID, Form>, Form : AbstractForm>(
    crudService: Service
) : TekCrudController<Entity, ID, Service, Form>(crudService) {

    @PreAuthorize("this.createAuthorized")
    @GetMapping("/list")
    @ApiPageable
    override fun list(@ApiIgnore pageable: Pageable, predicate: Predicate?): ResponseEntity<TekPageResponse<Entity>> {
        return super.list(pageable, predicate)
    }

    @PreAuthorize("this.readAuthorized")
    @GetMapping("/read/{id}")
    override fun read(id: ID): ResponseEntity<TekResponseEntity<Entity>> {
        return super.read(id)
    }

    @PreAuthorize("this.updateAuthorized")
    @PatchMapping("/update/{id}")
    override fun update(@RequestBody properties: Map<String, Any?>, @PathVariable("id") id: ID): ResponseEntity<TekResponseEntity<Entity>> {
        return super.update(properties, id)
    }

    @PreAuthorize("this.updateAuthorized")
    @PutMapping("/update/{id}")
    override fun update(@RequestBody @Valid form: Form, @PathVariable("id") id: ID): ResponseEntity<TekResponseEntity<Entity>> {
        return super.update(form, id)
    }

    @PreAuthorize("this.deleteAuthorized")
    @DeleteMapping("/delete/{id}")
    override fun delete(id: ID): ResponseEntity<TekResponseEntity<ID>> {
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