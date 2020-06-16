package com.tek.security.common.controller

import com.querydsl.core.types.Predicate
import com.tek.core.TekBaseResponse
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.core.controller.*
import com.tek.core.service.ReadOnlyCrudService
import com.tek.core.service.WritableCrudService
import com.tek.core.swagger.ApiPageable
import com.tek.security.common.TekRoleRegistry
import com.tek.security.common.hasRole
import com.tek.security.common.service.TekAuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import javax.validation.Valid

abstract class TekAuthReadOnlyController<Entity, ID, Service> :
    TekReadOnlyController<Entity, ID, Service>()
        where Entity : Any, Service : ReadOnlyCrudService<Entity, ID> {

    @Autowired
    lateinit var authService: TekAuthService

    @Autowired
    lateinit var roleRegistry: TekRoleRegistry

    val readAuthorized: Boolean
        get() = hasRole(roleRegistry.getRoleRead(service.entityClass))

    @PreAuthorize("this.readAuthorized")
    @GetMapping(READ_MAPPING)
    override fun read(@PathVariable("id") id: ID): ResponseEntity<TekResponseEntity<Entity>> {
        return super.read(id)
    }

    @PreAuthorize("this.readAuthorized")
    @GetMapping(LIST_MAPPING)
    @ApiPageable
    override fun list(
        @ApiIgnore pageable: Pageable,
        predicate: Predicate?
    ): ResponseEntity<TekPageResponse<Entity>> {
        return super.list(pageable, predicate)
    }
}

abstract class TekAuthWritableController<Entity, ID, Service, CreateForm, UpdateForm> :
    TekAuthReadOnlyController<Entity, ID, Service>(),
    WritablePort<Entity, ID, CreateForm, UpdateForm>
        where Entity : Any, Service : WritableCrudService<Entity, ID, CreateForm, UpdateForm>,
              CreateForm : CreatableForm,
              UpdateForm : UpdatableForm {

    val createAuthorized: Boolean
        get() = hasRole(roleRegistry.getRoleCreate(service.entityClass))

    val updateAuthorized: Boolean
        get() = hasRole(roleRegistry.getRoleUpdate(service.entityClass))

    val deleteAuthorized: Boolean
        get() = hasRole(roleRegistry.getRoleDelete(service.entityClass))

    @PreAuthorize("this.createAuthorized")
    @PostMapping(CREATE_MAPPING)
    override fun create(
        @Valid @RequestBody form: CreateForm
    ): ResponseEntity<TekResponseEntity<Entity>> {
        return ResponseEntity.ok(TekResponseEntity(HttpStatus.OK, service.create(form)))
    }

    @PreAuthorize("this.updateAuthorized")
    @PostMapping(UPDATE_MAPPING)
    override fun update(
        @Valid @RequestBody form: UpdateForm,
        @PathVariable("id") id: ID
    ): ResponseEntity<TekResponseEntity<Entity>> {
        return ResponseEntity.ok(TekResponseEntity(HttpStatus.OK, service.update(form, id)))
    }

    @PreAuthorize("this.deleteAuthorized")
    @DeleteMapping(DELETE_MAPPING)
    override fun delete(@PathVariable("id") id: ID): ResponseEntity<TekBaseResponse> {
        return ResponseEntity.ok(TekBaseResponse(HttpStatus.OK, service.delete(id)))
    }
}