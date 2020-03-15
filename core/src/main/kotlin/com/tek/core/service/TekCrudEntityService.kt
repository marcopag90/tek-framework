package com.tek.core.service

import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.StringExpression
import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekResourceNotFoundException
import com.tek.core.exception.TekServiceException
import com.tek.core.exception.TekValidationException
import com.tek.core.form.AbstractForm
import com.tek.core.i18n.CoreMessageSource
import com.tek.core.repository.TekEntityRepository
import com.tek.core.util.LoggerDelegate
import com.tek.core.util.orNull
import org.apache.commons.beanutils.BeanUtils
import org.apache.commons.beanutils.PropertyUtilsBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.validation.Validator

/**
 * Service to provide bean validations and access to [R] execute _CRUD_ operations over a given [E].
 */
@Component
abstract class TekCrudEntityService<E, ID, R, DTO : AbstractForm>(
    protected val stringIdExpression: StringExpression,
    protected val repository: R
) : ICrudEntityService<E, ID, DTO> where R : TekEntityRepository<E, ID> {

    protected val log by LoggerDelegate()

    @Autowired
    protected lateinit var validator: Validator
    @Autowired
    protected lateinit var coreMessageSource: CoreMessageSource

    override fun list(pageable: Pageable, predicate: Predicate?): Page<E> {
        log.debug("Fetching data from repository: $repository")

        predicate?.let {
            return repository.findAll(predicate, pageable)
        } ?: return repository.findAll(pageable)
    }

    @Transactional
    override fun readOne(id: ID): E {
        log.debug("Accessing $repository with id:$id")
        repository.findOne(stringIdExpression.eq(id.toString())).orNull()?.let { return it }
            ?: throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = CoreMessageSource.errorNotFoundResource,
                    parameters = arrayOf(id.toString())
                )
            )
    }

    @Transactional
    override fun update(properties: Map<String, Any?>, id: ID): E {
        log.debug("Initializing properties lookup for : $properties")
        if (properties.isNullOrEmpty()) throw TekServiceException(
            "At least one property must be set in properties: $properties",
            HttpStatus.NOT_ACCEPTABLE
        )

        log.debug("Accessing $repository with id:$id")
        val optional = repository.findOne(stringIdExpression.eq(id.toString()))
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

    @Transactional
    override fun update(form: DTO, id: ID): E {
        log.debug("Accessing $repository with id:$id")
        val optional = repository.findOne(stringIdExpression.eq(id.toString()))
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

    @Transactional
    override fun delete(id: ID) {
        log.debug("Accessing $repository with id:$id")
        repository.findOne(stringIdExpression.eq(id.toString())).orNull()?.let {
            repository.deleteById(id)
        } ?: throw TekResourceNotFoundException(
            data = ServiceExceptionData(
                source = coreMessageSource,
                message = CoreMessageSource.errorNotFoundResource,
                parameters = arrayOf(id.toString())
            )
        )
    }
}