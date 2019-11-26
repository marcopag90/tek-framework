package it.jbot.core.port

import it.jbot.core.JBotResponse
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

/**
 * Port Model for all base REST controllers
 */
interface JbotRestPort {

    fun postCreate(entityName: String, properties: Map<String, Any?>): ResponseEntity<JBotResponse>

    fun getList(entityName: String, pageable: Pageable): ResponseEntity<JBotResponse>

    fun getRead(entityName: String, id: Long): ResponseEntity<JBotResponse>

    fun patchUpdate(entityName: String, id: Long, properties: Map<String, Any?>): ResponseEntity<JBotResponse>

    fun deleteRemove(entityName: String, id: Long): ResponseEntity<JBotResponse>
}