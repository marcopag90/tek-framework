package it.jbot.core.service

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotResponseEntity
import it.jbot.core.JBotPageResponse
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

/**
 * Interface for all _CRUD_ operations over a given [E].
 *
 * Each implementation can provide read/write access to an [E] data in different ways:
 * 1) Spring Repository Data Interface
 * 2) Web Service
 * 3) Cloud Service
 * 4) etc...
 */
interface ICrudService<E, ID, DTO> {

    fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<E>>

    fun update(properties: Map<String, Any?>, id: ID): ResponseEntity<JBotResponseEntity<E>>

    fun update(dto: DTO, id: ID): ResponseEntity<JBotResponseEntity<E>>
}