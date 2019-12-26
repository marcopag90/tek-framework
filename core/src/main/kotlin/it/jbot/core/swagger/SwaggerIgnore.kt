package it.jbot.core.swagger

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

/**
 * Parameters to be ignored by Swagger
 */
object SwaggerIgnore {

    fun ignoredParameters(): Array<Class<out Any>> {
        return arrayOf(
            Pageable::class.java,
            Page::class.java,
            Sort::class.java
        )
    }

}