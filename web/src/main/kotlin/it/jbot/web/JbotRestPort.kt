package it.jbot.web

import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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