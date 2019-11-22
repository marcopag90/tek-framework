package it.jbot.web

import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

/**
 * Port Endpoint for all base _CRUD_ operations
 */
interface JbotRestPort {

    fun create(entityName: String, properties: Map<String, Any?>): ResponseEntity<JBotResponse>

    fun list(entityName: String, pageable: Pageable): ResponseEntity<JBotResponse>

    fun read(entityName: String, id: Long): ResponseEntity<JBotResponse>

    fun update(entityName: String, id: Long, properties: Map<String, Any?>): ResponseEntity<JBotResponse>

    fun delete(entityName: String, id: Long): ResponseEntity<JBotResponse>
}