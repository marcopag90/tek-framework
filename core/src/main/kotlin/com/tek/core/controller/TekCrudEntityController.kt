package com.tek.core.controller

import com.querydsl.core.types.Predicate
import com.tek.core.TekBaseResponse
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.core.form.AbstractForm
import com.tek.core.service.ICrudEntityService
import com.tek.core.swagger.ApiPageable
import com.tek.core.util.LoggerDelegate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import javax.validation.Valid

/**
 * Generic _REST_ Controller to provide common JPA standard _CRUD_ operations for a given [E] with a specific [ID].
 * To execute a crud request, every entity must:
 * 1) provide a [org.springframework.stereotype.Service] extending [com.tek.core.service.TekCrudEntityService]
 * 2) extend the [TekCrudEntityController] in a [org.springframework.web.bind.annotation.RestController] for the given entity
 * 3) override method list to add annotation @QueryDslPredicate (QueryDsl open bug) and call super method
 */
@Component
abstract class TekCrudEntityController<E, ID, S : ICrudEntityService<E, ID, DTO>,
        DTO : AbstractForm> : ICrudEntityController<E, ID, DTO> {

    protected val log by LoggerDelegate()

    @Autowired
    protected lateinit var service: S

    /**
     * Function to query an [E] via [ICrudEntityService].
     *
     * Given a [Pageable] and a [Predicate] binded from client calls,
     * it returns a [org.springframework.data.domain.Page] of [E] type.
     */
    @GetMapping("/list")
    @ApiPageable
    override fun list(@ApiIgnore pageable: Pageable, predicate: Predicate?): ResponseEntity<TekPageResponse<E>> {
        log.debug("Executing method: {}", RequestMethod.GET)
        return ResponseEntity.ok(
            TekPageResponse(HttpStatus.OK, service.list(pageable, predicate))
        )
    }

    /**
     * Function to execute a _GET_ request for the the given [E] with id [ID] via [ICrudEntityService].
     */
    @GetMapping("/read/{id}")
    override fun read(@PathVariable("id") id: ID): ResponseEntity<TekResponseEntity<E>> {
        log.debug("Executing method: {}", RequestMethod.GET)
        return ResponseEntity.ok(
            TekResponseEntity(HttpStatus.OK, service.readOne(id))
        )
    }

    /**
     * Function to execute a _PATCH_ request for the given [E] via [ICrudEntityService].
     */
    @PatchMapping("/update/{id}")
    override fun update(@RequestBody properties: Map<String, Any?>, @PathVariable("id") id: ID): ResponseEntity<TekResponseEntity<E>> {
        log.debug("Executing method: {}", RequestMethod.PATCH)
        return ResponseEntity.ok(
            TekResponseEntity(HttpStatus.OK, service.update(properties, id))
        )
    }

    /**
     * Function to execute a _POST_ request for the given [E] via [ICrudEntityService].
     */
    @PostMapping("/update/{id}")
    override fun update(@RequestBody @Valid form: DTO, @PathVariable("id") id: ID): ResponseEntity<TekResponseEntity<E>> {
        log.debug("Executing method: {}", RequestMethod.PUT)
        return ResponseEntity.ok(
            TekResponseEntity(HttpStatus.OK, service.update(form, id))
        )
    }

    /**
     * Function to execute a _DELETE_ request for the given [E] via [ICrudEntityService]
     */
    @DeleteMapping("/delete/{id}")
    override fun delete(@PathVariable("id") id: ID): ResponseEntity<TekBaseResponse> {
        log.debug("Executing method: {}", RequestMethod.DELETE)
        return ResponseEntity.ok(
            TekBaseResponse(HttpStatus.OK, service.delete(id))
        )
    }
}