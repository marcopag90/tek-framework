package it.jbot.core.controller

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotEntityResponse
import it.jbot.core.JBotPageResponse
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

interface ICrudController<Entity, Id, DTO> {

    fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<Entity>>

    fun update(properties: Map<String, Any?>, id: Id): ResponseEntity<JBotEntityResponse<Entity>>

    fun update(dto: DTO, id: Id): ResponseEntity<JBotEntityResponse<Entity>>

}