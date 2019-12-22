package it.jbot.core.controller

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotEntityResponse
import it.jbot.core.JBotPageResponse
import it.jbot.core.service.CrudService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable

abstract class CrudController<Entity, Id>(
    open val service: CrudService<Entity, Id>
) : CrudService<Entity, Id> {

    /**
     * Function to query an [Entity] provided in a [CrudService].
     *
     * Given a [Pageable] and a [Predicate] binded from client calls,
     * it returns a [org.springframework.data.domain.Page] of [Entity] type.
     */
    override fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<Entity>> =
        service.list(pageable, predicate)

    /**
     * Function that _MUST_ be implemented in order to allow [org.springframework.data.querydsl.binding.QuerydslPredicate]
     * to dynamically resolve the root of the entity, since the generic representation is just Object.class (due to Querydsl limitation).
     * In the implementation, add the annotation to predicate parameter and just call [list].
     */
    @GetMapping("/list")
    abstract fun listResolve(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<Entity>>

    /**
     * Function to update an [Entity] provided in a [CrudService].
     */
    override fun update(properties: Map<String, Any?>, id: Id): ResponseEntity<JBotEntityResponse<Entity>> =
        service.update(properties, id)


}