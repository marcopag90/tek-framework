package com.tek.core.config.swagger;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams(
    value = {
        @ApiImplicitParam(
            paramType = "query",
            /*
            Swagger bug: dataType int with defaultValue = 0 fails to escape html.
            Workaround to use it as string parameter
            */
            dataTypeClass = String.class,
            name = "page",
            value = "The page number you want to retrieve.",
            defaultValue = "0",
            example = "0"
        ),
        @ApiImplicitParam(
            paramType = "query",
            dataTypeClass = Integer.class,
            name = "size",
            value = "Number of records per page.",
            defaultValue = "10",
            example = "10"
        ),
        @ApiImplicitParam(
            paramType = "query",
            dataTypeClass = String.class,
            name = "sort",
            value = "Sorting criteria in the format: property,asc|desc. " +
                "Default sort order is ascending. Multiple sort criteria is supported.",
            allowMultiple = true
        )
    }
)
public @interface ApiPageable {
}
