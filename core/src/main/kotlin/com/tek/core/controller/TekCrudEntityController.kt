package com.tek.core.controller

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.core.form.AbstractForm
import com.tek.core.service.ICrudEntityService
import com.tek.core.swagger.ApiPageable
import com.tek.core.util.LoggerDelegate
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import javax.validation.Valid

/**
 * Generic _REST_ Controller to provide common JPA standard _CRUD_ operations for a given [Entity] with a specific [ID].
 * To execute a crud request, every entity must:
 * 1) provide a [org.springframework.stereotype.Repository] extending [com.tek.core.repository.TekEntityRepository]
 * 2) provide a [org.springframework.stereotype.Service] extending [com.tek.core.service.TekCrudEntityService]
 * 3) extend the [TekCrudEntityController] in a [org.springframework.web.bind.annotation.RestController] for the given entity
 */
abstract class TekCrudEntityController<Entity, ID, Service : ICrudEntityService<Entity, ID, Form>, Form : AbstractForm>(
    protected open val crudService: Service
) : ICrudEntityController<Entity, ID, Form> {

    protected val log by LoggerDelegate()

    /**
     * Function to query an [Entity] via [ICrudEntityService].
     *
     * Given a [Pageable] and a [Predicate] binded from client calls,
     * it returns a [org.springframework.data.domain.Page] of [Entity] type.
     */
    @GetMapping("/list")
    @ApiPageable
    override fun list(@ApiIgnore pageable: Pageable, predicate: Predicate?): ResponseEntity<TekPageResponse<Entity>> {
        log.debug("Executing [GET] method")
        return ResponseEntity.ok(
            TekPageResponse(HttpStatus.OK, crudService.list(pageable, predicate))
        )
    }

    /**
     * Function to execute a _GET_ request for the the given [Entity] with id [ID] via [ICrudEntityService].
     */
    @GetMapping("/read/{id}")
    override fun read(@PathVariable("id") id: ID): ResponseEntity<TekResponseEntity<Entity>> {
        log.debug("Executing [GET] method")
        return ResponseEntity.ok(
            TekResponseEntity(HttpStatus.OK, crudService.readOne(id))
        )
    }

    /**
     * Function to execute a _PATCH_ request for the given [Entity] via [ICrudEntityService].
     */
    @PatchMapping("/update/{id}")
    override fun update(@RequestBody properties: Map<String, Any?>, @PathVariable("id") id: ID): ResponseEntity<TekResponseEntity<Entity>> {
        log.debug("Executing [PATCH] method")
        return ResponseEntity.ok(
            TekResponseEntity(HttpStatus.OK, crudService.update(properties, id))
        )
    }

    /**
     * Function to execute a _PUT_ request for the given [Entity] via [ICrudEntityService].
     */
    @PutMapping("/update/{id}")
    override fun update(@RequestBody @Valid form: Form, @PathVariable("id") id: ID): ResponseEntity<TekResponseEntity<Entity>> {
        log.debug("Executing [PUT] method")
        return ResponseEntity.ok(
            TekResponseEntity(HttpStatus.OK, crudService.update(form, id))
        )
    }

    /**
     * Function to execute a _DELETE_ request for the given [Entity] via [ICrudEntityService]
     */
    @DeleteMapping("/delete/{id}")
    override fun delete(@PathVariable("id") id: ID): ResponseEntity<TekResponseEntity<Unit>> {
        log.debug("Executing [DELETE] method")
        return ResponseEntity.ok(
            TekResponseEntity(HttpStatus.OK, crudService.delete(id))
        )
    }


}