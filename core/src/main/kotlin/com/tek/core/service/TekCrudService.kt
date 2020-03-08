package com.tek.core.service

import com.querydsl.core.types.Predicate
import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekResourceNotFoundException
import com.tek.core.exception.TekServiceException
import com.tek.core.exception.TekValidationException
import com.tek.core.form.AbstractForm
import com.tek.core.i18n.CoreMessageSource
import com.tek.core.repository.TekRepository
import com.tek.core.util.LoggerDelegate
import org.apache.commons.beanutils.BeanUtils
import org.apache.commons.beanutils.PropertyUtilsBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import javax.validation.Validator

/**
 * Service to provide bean validations and access to [TekRepository] to execute _CRUD_ operations over a given [Entity].
 */
abstract class TekCrudService<Entity, ID, Repository : TekRepository<Entity, ID>, DTO : AbstractForm>(
    protected open val entityClass: Class<Entity>,
    protected open val repository: Repository,
    protected open val validator: Validator,
    protected open val coreMessageSource: CoreMessageSource
) : ICrudService<Entity, ID, DTO> {

    protected val log by LoggerDelegate()

    override fun list(pageable: Pageable, predicate: Predicate?): Page<Entity> {
        log.debug("Fetching data from repository: $repository")

        predicate?.let {
            return repository.findAll(predicate, pageable)
        } ?: return repository.findAll(pageable)
    }

    override fun readOne(id: ID): Entity {
        log.debug("Accessing $repository for entity: ${entityClass::class.java.name} with id:$id")

        val optional = repository.findById(id)
        if (!optional.isPresent)
            throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = CoreMessageSource.errorNotFoundResource,
                    parameters = arrayOf(id.toString())
                )
            )
        return optional.get()
    }

    override fun update(properties: Map<String, Any?>, id: ID): Entity {
        log.debug("Initializing properties lookup for : $properties")

        if (properties.isNullOrEmpty()) throw TekServiceException(
            "At least one property must be set in properties: $properties",
            HttpStatus.NOT_ACCEPTABLE
        )

        log.debug("Accessing $repository for entity: $entityClass with id:$id")
        val optional = repository.findById(id)
        if (!optional.isPresent)
            throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = CoreMessageSource.errorNotFoundResource,
                    parameters = arrayOf(id.toString())
                )
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

        return repository.save(instance)
    }

    override fun update(form: DTO, id: ID): Entity {
        log.debug("Accessing $repository for entity: $entityClass with id:$id")

        val optional = repository.findById(id)
        if (!optional.isPresent)
            throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = CoreMessageSource.errorNotFoundResource,
                    parameters = arrayOf(id.toString())
                )
            )
        val entityToUpdate = optional.get()

        log.debug("Filling properties:$form in class instance:$entityToUpdate")
        BeanUtils.copyProperties(entityToUpdate, form)
        log.debug("Properties successfully loaded in class instance.")

        return repository.save(entityToUpdate)
    }

    override fun delete(id: ID): ID {
        log.debug("Accessing $repository for entity: $entityClass with id:$id")

        val optional = repository.findById(id)
        if (!optional.isPresent)
            throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = CoreMessageSource.errorNotFoundResource,
                    parameters = arrayOf(id.toString())
                )
            )
        repository.deleteById(id)
        return id
    }
}