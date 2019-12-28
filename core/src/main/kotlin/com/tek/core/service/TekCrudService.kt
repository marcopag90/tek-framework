package com.tek.core.service

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.core.exception.TekServiceException
import com.tek.core.exception.TekValidationException
import com.tek.core.form.AbstractDTO
import com.tek.core.repository.TekRepository
import com.tek.core.util.LoggerDelegate
import org.apache.commons.beanutils.BeanUtils
import org.apache.commons.beanutils.PropertyUtilsBean
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import javax.validation.Validator

/**
 * Service to provide bean validations and access to [TekRepository] to execute _CRUD_ operations over a given [E].
 */
abstract class TekCrudService<E, ID, R : TekRepository<E, ID>, DTO : AbstractDTO>(
    protected val entityClass: Class<E>,
    protected val repository: R,
    protected val validator: Validator
) : ICrudService<E, ID, DTO> {

    protected val log by LoggerDelegate()

    override fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<TekPageResponse<E>> {

        log.debug("Fetching data from repository: $repository")
        predicate?.let {
            return ResponseEntity(
                TekPageResponse(
                    HttpStatus.OK,
                    repository.findAll(predicate, pageable)
                ),
                HttpStatus.OK
            )
        } ?: return ResponseEntity(
            TekPageResponse(
                HttpStatus.OK,
                repository.findAll(pageable)
            ), HttpStatus.OK)
    }

    override fun update(properties: Map<String, Any?>, id: ID): ResponseEntity<TekResponseEntity<E>> {

        log.debug("Initializing properties lookup for : $properties")
        if (properties.isNullOrEmpty()) throw TekServiceException(
            "At least one property must be set in properties: $properties",
            HttpStatus.NOT_ACCEPTABLE
        )

        log.debug("Accessing $repository for entity: $entityClass with id:$id")
        val optional = repository.findById(id)
        if (!optional.isPresent)
            throw TekServiceException(
                "Entity $entityClass with id:$id not found",
                HttpStatus.NOT_FOUND
            )

        val instance = optional.get()
        val entityProps = PropertyUtilsBean().describe(instance)
        properties.forEach { (k, _) ->
            if (!entityProps.containsKey(k)) throw TekServiceException(
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
            throw TekValidationException(violationMap)
        }
        log.debug("Properties successfully loaded in class instance.")
        val updatedEntity = repository.save(instance)
        log.debug("Update success!")

        return ResponseEntity(TekResponseEntity(HttpStatus.OK, updatedEntity), HttpStatus.OK)
    }

    override fun update(dto: DTO, id: ID): ResponseEntity<TekResponseEntity<E>> {

        log.debug("Accessing $repository for entity: $entityClass with id:$id")
        val optional = repository.findById(id)
        if (!optional.isPresent) throw TekServiceException(
            "Entity $entityClass with id:$id not found",
            HttpStatus.NOT_FOUND
        )
        val entityToUpdate = optional.get()

        log.debug("Filling properties:$dto in class instance:$entityToUpdate")
        BeanUtils.copyProperties(entityToUpdate, dto)
        log.debug("Properties successfully loaded in class instance.")

        val updatedEntity = repository.save(entityToUpdate)
        log.debug("Update success for entity: $updatedEntity")

        return ResponseEntity(TekResponseEntity(HttpStatus.OK, updatedEntity), HttpStatus.OK)
    }
}