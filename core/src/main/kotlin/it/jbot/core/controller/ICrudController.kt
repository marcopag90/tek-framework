package it.jbot.core.controller

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotResponseEntity
import it.jbot.core.JBotPageResponse
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

interface ICrudController<E, ID, DTO> {

    fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<E>>

    fun update(properties: Map<String, Any?>, id: ID): ResponseEntity<JBotResponseEntity<E>>

    fun update(dto: DTO, id: ID): ResponseEntity<JBotResponseEntity<E>>

}