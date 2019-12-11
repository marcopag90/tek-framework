package it.jbot.core.validation

import org.springframework.stereotype.Component
import javax.validation.ConstraintViolation
import javax.validation.Validator

@Component
class BeanValidator(
    private val validator: Validator
) {

    fun validate(bean: Any): MutableSet<ConstraintViolation<Any>> = validator.validate(bean)

}