package it.jbot.core.controller

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotEntityResponse
import it.jbot.core.JBotPageResponse
import it.jbot.core.service.ICrudService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

/**
 * Generic _REST_ Controller to provide common JPA standard _CRUD_ operations for a given [Entity] with a specific [Id].
 * To execute a crud request, every entity must:
 * 1) provide a [org.springframework.stereotype.Repository] extending [it.jbot.core.repository.JBotRepository]
 * 2) provide a [org.springframework.stereotype.Service] extending [it.jbot.core.service.JBotCrudService]
 * 3) extend the [JBotCrudController] in a [org.springframework.web.bind.annotation.RestController] for the given entity
 */
//TODO crudService to be private? need to refactor all common methods here!
abstract class JBotCrudController<Entity, Id, CrudService : ICrudService<Entity, Id>>(
    protected val crudService: CrudService
) : ICrudService<Entity, Id> {

    companion object {
        protected val log: Logger = LoggerFactory.getLogger(JBotCrudController::class.java)
    }

    /**
     * Function to query an [Entity] via [ICrudService].
     *
     * Given a [Pageable] and a [Predicate] binded from client calls,
     * it returns a [org.springframework.data.domain.Page] of [Entity] type.
     */
    @GetMapping("/list")
    override fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<Entity>> {
        log.debug("Executing [list] method")
        return crudService.list(pageable, predicate)
    }

    /**
     * Function to update an [Entity] via [ICrudService].
     */
    @PatchMapping("/update/{id}")
    override fun update(@RequestBody properties: Map<String, Any?>, @PathVariable("id") id: Id): ResponseEntity<JBotEntityResponse<Entity>> {
        log.debug("Executing [update] method")
        return crudService.update(properties, id)
    }
}