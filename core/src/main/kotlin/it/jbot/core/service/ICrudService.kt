package it.jbot.core.service

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotEntityResponse
import it.jbot.core.JBotPageResponse
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

/**
 * Interface for all _CRUD_ operations over a given [Entity].
 *
 * Each implementation can provide read/write access to an [Entity] data in different ways:
 * 1) Spring Repository Data Interface
 * 2) Web Service
 * 3) Cloud Service
 * 4) etc...
 */
interface ICrudService<Entity, Id, DTO> {

    fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<Entity>>

    fun update(properties: Map<String, Any?>, id: Id): ResponseEntity<JBotEntityResponse<Entity>>

    fun update(dto: DTO, id: Id): ResponseEntity<JBotEntityResponse<Entity>>
}