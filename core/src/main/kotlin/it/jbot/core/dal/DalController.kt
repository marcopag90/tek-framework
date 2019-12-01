package it.jbot.core.dal

import it.jbot.core.JBotResponse
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.RestController

@RestController
class DalController(
    private val configuration: DalServiceConfiguration
) : DalPort {

    override fun create(
        entityName: String,
        properties: Map<String, Any?>
    ): ResponseEntity<JBotResponse> =
        configuration.getDalService(entityName).create(properties)

    override fun list(
        entityName: String,
        properties: MultiValueMap<String, String>,
        pageable: Pageable
    ): ResponseEntity<JBotResponse> =
        configuration.getDalService(entityName).list(properties, pageable)


    override fun read(entityName: String, id: Long): ResponseEntity<JBotResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(entityName: String, id: Long, properties: Map<String, Any?>): ResponseEntity<JBotResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(entityName: String, id: Long): ResponseEntity<JBotResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}