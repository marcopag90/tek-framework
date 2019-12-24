package it.jbot.core.controller

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotResponseEntity
import it.jbot.core.JBotPageResponse
import it.jbot.core.form.AbstractDTO
import it.jbot.core.service.ICrudService
import it.jbot.core.util.LoggerDelegate
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * Generic _REST_ Controller to provide common JPA standard _CRUD_ operations for a given [E] with a specific [ID].
 * To execute a crud request, every entity must:
 * 1) provide a [org.springframework.stereotype.Repository] extending [it.jbot.core.repository.JBotRepository]
 * 2) provide a [org.springframework.stereotype.Service] extending [it.jbot.core.service.JBotCrudService]
 * 3) extend the [JBotCrudController] in a [org.springframework.web.bind.annotation.RestController] for the given entity
 */
abstract class JBotCrudController<E, ID, S : ICrudService<E, ID, DTO>, DTO : AbstractDTO>(
    protected val crudService: S
) : ICrudController<E, ID, DTO> {

    protected val log by LoggerDelegate()

    /**
     * Function to query an [E] via [ICrudService].
     *
     * Given a [Pageable] and a [Predicate] binded from client calls,
     * it returns a [org.springframework.data.domain.Page] of [E] type.
     */
    @GetMapping("/list")
    override fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<E>> {
        log.debug("Executing [GET] method")
        return crudService.list(pageable, predicate)
    }

    /**
     * Function to execute a _PATCH_ request for the given [E] via [ICrudService].
     */
    @PatchMapping("/update/{id}")
    override fun update(@RequestBody properties: Map<String, Any?>, @PathVariable("id") id: ID): ResponseEntity<JBotResponseEntity<E>> {
        log.debug("Executing [PATCH] method")
        return crudService.update(properties, id)
    }

    /**
     * Function to execute a _PUT_ request for the given [E] via [ICrudService].
     */
    @PutMapping("/update/{id}")
    override fun update(@RequestBody @Valid dto: DTO, @PathVariable("id") id: ID): ResponseEntity<JBotResponseEntity<E>> {
        log.debug("Executing [PUT] method")
        return crudService.update(dto, id)
    }

}