package it.jbot.core.port.controller

import it.jbot.core.JBotResponse
import it.jbot.core.port.JbotRestPort
import it.jbot.core.configuration.JBotAdapterConfiguration
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/crud/{entityName}")
class JbotRestController(
    private val configuration: JBotAdapterConfiguration
) : JbotRestPort {

    @PostMapping
    override fun postCreate(
        @PathVariable("entityName") entityName: String,
        @RequestBody properties: Map<String, Any?>
    ): ResponseEntity<JBotResponse> =
        configuration.getAdapter(entityName).createEntity(properties)

    override fun getList(entityName: String, pageable: Pageable): ResponseEntity<JBotResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRead(entityName: String, id: Long): ResponseEntity<JBotResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun patchUpdate(entityName: String, id: Long, properties: Map<String, Any?>): ResponseEntity<JBotResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRemove(entityName: String, id: Long): ResponseEntity<JBotResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}