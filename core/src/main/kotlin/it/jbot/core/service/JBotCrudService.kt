package it.jbot.core.service

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotEntityResponse
import it.jbot.core.JBotPageResponse
import it.jbot.core.repository.JBotRepository
import it.jbot.core.validation.EntityValidator
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 * Service to provide bean validations and access to [JBotRepository] to execute _CRUD_ operations over a given [Entity].
 */
abstract class JBotCrudService<Entity, Id, Repository : JBotRepository<Entity, Id>>(
    open val repository: Repository,
    open val validator: EntityValidator
) : ICrudService<Entity, Id> {

    override fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<Entity>> {
        predicate?.let {
            return ResponseEntity(
                JBotPageResponse(HttpStatus.OK, repository.findAll(predicate, pageable)),
                HttpStatus.OK
            )
        } ?: return ResponseEntity(JBotPageResponse(HttpStatus.OK, repository.findAll(pageable)), HttpStatus.OK)
    }

    override fun update(properties: Map<String, Any?>, id: Id): ResponseEntity<JBotEntityResponse<Entity>> {
        TODO("not implemented")
    }
}