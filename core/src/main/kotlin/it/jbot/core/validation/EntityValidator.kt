package it.jbot.core.validation

import it.jbot.core.exception.JBotValidationException
import it.jbot.core.util.entityToMap
import org.apache.commons.beanutils.BeanUtils
import org.springframework.stereotype.Component
import javax.validation.ConstraintViolation
import javax.validation.Validator

@Component
class EntityValidator(
    private val validator: Validator
) {

    fun <Entity : Any> triggerValidation(entity: Entity): MutableSet<ConstraintViolation<Entity>> =
        validator.validate(entity)

    fun <Entity : Any> describe(entity: Entity): Map<String, Any?> = entityToMap(entity)

    fun <Entity : Any> fillProperties(bean: Entity, properties: Map<String, Any?>) =
        BeanUtils.populate(bean, properties)

    //TODO check validation
    fun <Entity : Any> getUpdatableEntity(properties: Map<String, Any?>, entity: Entity): Entity {
        val entityProps = describe(entity)
        properties.forEach { (k, _) ->
            requireNotNull(entityProps[k])
        }
        fillProperties(entity, properties)
        val violations = triggerValidation(entity)
        if (violations.isNotEmpty()) {
            val violationMap = mutableMapOf<String, String>()
            for (v in violations)
                violationMap[v.propertyPath.toList()[0].name] = v.message
            throw JBotValidationException(violationMap)
        }
        return entity
    }
}