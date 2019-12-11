package it.jbot.core.web

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotPageEntityResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping

abstract class CrudController<Entity>(
    open val service: UpdatableService<Entity>
) : UpdatableService<Entity> {

    @GetMapping("list")
    abstract fun list(pageable: Pageable, @QuerydslPredicate predicate: Predicate?): ResponseEntity<JBotPageEntityResponse<Entity>>

    @PatchMapping("/update")
    override fun <Entity> update(properties: Map<String, Any?>): Entity = service.update(properties)
}


interface UpdatableService<Entity> {
    fun <Entity> update(properties: Map<String, Any?>): Entity
}