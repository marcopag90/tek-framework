package it.jbot.core

import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import javax.validation.Validation
import javax.validation.Validator

/**
 * Abstract representation of a JBot Service
 *
 * The service is linked to an entity via [org.springframework.stereotype.Service].
 *
 * The service qualifier has to match the entity level annotation [DalService].
 *
 * The service implementation over an entity provides all base _CRUD_ operations for it.
 */
abstract class DalService<Entity : Any> {

    var validator: Validator = Validation.buildDefaultValidatorFactory().validator

    abstract fun create(properties: Map<String, Any?>): ResponseEntity<JBotResponse>

    abstract fun list(properties: MultiValueMap<String, String>, pageable: Pageable): ResponseEntity<JBotResponse>

}