package it.jbot.web.controller

import it.jbot.core.component.JBotCrudHelper
import it.jbot.web.JBotResponse
import it.jbot.web.JbotRestPort
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * Rest Controller definition for base Rest Endpoint.
 *
 */
@RestController
@RequestMapping("/crud/{entityName}")
@PreAuthorize("hasRole('ROLE_USER')")
class JBotRestController(
    private val crudHelper: JBotCrudHelper
) : JbotRestPort {

    @PostMapping
    override fun create(
        @PathVariable("entityName") entityName: String,
        @RequestBody properties: Map<String, Any?>
    ): ResponseEntity<JBotResponse> {
        return ResponseEntity(
            JBotResponse(HttpStatus.OK).apply {
                this.result = null
            },
            HttpStatus.OK
        )
    }

    @GetMapping
    override fun list(
        @PathVariable("entityName") entityName: String,
        pageable: Pageable
    ): ResponseEntity<JBotResponse> {
        return ResponseEntity(
            JBotResponse(HttpStatus.OK, crudHelper.getJpaRepository(entityName).findAll(pageable)),
            HttpStatus.OK
        )
    }

    @GetMapping("/{id}")
    override fun read(
        @PathVariable("entityName") entityName: String,
        @PathVariable("id") id: Long
    ): ResponseEntity<JBotResponse> {
        return ResponseEntity(
            JBotResponse(HttpStatus.OK).apply {
                this.result = crudHelper.entityManager.find(crudHelper.getEntityClass(entityName), id)
            },
            HttpStatus.OK
        )
    }

    @PatchMapping
    override fun update(
        @PathVariable("entityName") entityName: String,
        @PathVariable("id") id: Long,
        @RequestBody properties: Map<String, Any?>
    ): ResponseEntity<JBotResponse> {
        return ResponseEntity(
            JBotResponse(HttpStatus.OK).apply {
                this.result = null
            },
            HttpStatus.OK
        )
    }

    @DeleteMapping
    override fun delete(
        @PathVariable("entityName") entityName: String,
        @PathVariable("id") id: Long
    ): ResponseEntity<JBotResponse> {
        return ResponseEntity(
            JBotResponse(HttpStatus.OK).apply {
                this.result = crudHelper.entityManager.find(crudHelper.getEntityClass(entityName), id)
                    ?.let { crudHelper.entityManager.remove(it) }
            },
            HttpStatus.OK
        )
    }

}