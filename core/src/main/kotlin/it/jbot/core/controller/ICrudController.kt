package it.jbot.core.controller

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotEntityResponse
import it.jbot.core.JBotPageResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable

interface ICrudController<Entity, Id> {

    @GetMapping("/list")
    fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<Entity>>

    @PatchMapping("/update/{id}")
    fun update(properties: Map<String, Any?>, @PathVariable("id") id: Id): ResponseEntity<JBotEntityResponse<Entity>>
}