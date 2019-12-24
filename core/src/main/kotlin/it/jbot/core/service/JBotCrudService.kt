package it.jbot.core.service

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotResponseEntity
import it.jbot.core.JBotPageResponse
import it.jbot.core.exception.JBotServiceException
import it.jbot.core.exception.JBotValidationException
import it.jbot.core.form.AbstractDTO
import it.jbot.core.repository.JBotRepository
import it.jbot.core.util.LoggerDelegate
import org.apache.commons.beanutils.BeanUtils
import org.apache.commons.beanutils.PropertyUtilsBean
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import javax.validation.Validator

/**
 * Service to provide bean validations and access to [JBotRepository] to execute _CRUD_ operations over a given [E].
 */
abstract class JBotCrudService<E, ID, R : JBotRepository<E, ID>, DTO : AbstractDTO>(
    protected val entityClass: Class<E>,
    protected val repository: R,
    protected val validator: Validator
) : ICrudService<E, ID, DTO> {

    protected val log by LoggerDelegate()

    override fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<E>> {

        log.debug("Fetching data from repository: $repository")
        predicate?.let {
            return ResponseEntity(
                JBotPageResponse(HttpStatus.OK, repository.findAll(predicate, pageable)),
                HttpStatus.OK
            )
        } ?: return ResponseEntity(JBotPageResponse(HttpStatus.OK, repository.findAll(pageable)), HttpStatus.OK)
    }

    override fun update(properties: Map<String, Any?>, id: ID): ResponseEntity<JBotResponseEntity<E>> {

        log.debug("Initializing properties lookup for : $properties")
        if (properties.isNullOrEmpty()) throw JBotServiceException(
            "At least one property must be set in properties: $properties",
            HttpStatus.NOT_ACCEPTABLE
        )

        log.debug("Accessing $repository for entity: $entityClass with id:$id")
        val optional = repository.findById(id)
        if (!optional.isPresent)
            throw JBotServiceException("Entity $entityClass with id:$id not found", HttpStatus.NOT_FOUND)

        val instance = optional.get()
        val entityProps = PropertyUtilsBean().describe(instance)
        properties.forEach { (k, _) ->
            if (!entityProps.containsKey(k)) throw JBotServiceException(
                "property: $k not found in entity $instance",
                HttpStatus.NOT_ACCEPTABLE
            )
        }

        log.debug("Filling properties in class instance: $instance")
        BeanUtils.populate(instance, properties)

        val violations = validator.validate(instance)
        if (violations.isNotEmpty()) {
            val violationMap = mutableMapOf<String, String>()
            for (v in violations)
                violationMap[v.propertyPath.toList()[0].name] = v.message
            throw JBotValidationException(violationMap)
        }
        log.debug("Properties successfully loaded in class instance.")
        val updatedEntity = repository.save(instance)
        log.debug("Update success!")

        return ResponseEntity(JBotResponseEntity(HttpStatus.OK, updatedEntity), HttpStatus.OK)
    }

    override fun update(dto: DTO, id: ID): ResponseEntity<JBotResponseEntity<E>> {

        log.debug("Accessing $repository for entity: $entityClass with id:$id")
        val optional = repository.findById(id)
        if (!optional.isPresent) throw JBotServiceException(
            "Entity $entityClass with id:$id not found",
            HttpStatus.NOT_FOUND
        )
        val entityToUpdate = optional.get()

        log.debug("Filling properties:$dto in class instance:$entityToUpdate")
        BeanUtils.copyProperties(entityToUpdate, dto)
        log.debug("Properties successfully loaded in class instance.")

        val updatedEntity = repository.save(entityToUpdate)
        log.debug("Update success for entity: $updatedEntity")

        return ResponseEntity(JBotResponseEntity(HttpStatus.OK, updatedEntity), HttpStatus.OK)
    }
}