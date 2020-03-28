package com.tek.core.swagger

import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams

/**
 * Custom annotation to allow [org.springframework.data.domain.Pageable] query parameters to work properly.
 *
 * Feature working in Swagger only for Spring Data Rest. Waiting for a fix.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@ApiImplicitParams(
    value = [
        ApiImplicitParam(
            name = "page",
            dataType = "string", //swagger bug: dataType int with defaultValue = 0 fails to escape html. Workaround to use it as string parameter.
            paramType = "query",
            defaultValue = "0",
            example = "0",
            value = "Results page you want to retrieve."
        ),
        ApiImplicitParam(
            name = "size",
            dataType = "int",
            paramType = "query",
            defaultValue = "20",
            example = "20",
            value = "Number of records per page."
        ),
        ApiImplicitParam(
            name = "sort",
            allowMultiple = true,
            dataType = "string",
            paramType = "query",
            value = "Sorting criteria in the format: property,asc|desc. " +
                    "Default sort order is ascending. Multiple sort criteria are supported."
        )
    ]
)
annotation class ApiPageable

