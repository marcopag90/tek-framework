package com.tek.rest.shared.swagger;

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
            name = "page",
            value = "The page number you want to retrieve.",
            dataTypeClass = String.class,
            paramType = "query",
            /*
            Swagger bug: dataType int with defaultValue = 0 fails to escape html.
            Workaround to use it as string parameter
            */
            defaultValue = "0",
            example = "0"
        ),
        @ApiImplicitParam(
            name = "size",
            value = "Number of records per page.",
            dataTypeClass = Integer.class,
            paramType = "query",
            defaultValue = "10",
            example = "10"
        ),
        @ApiImplicitParam(
            name = "sort",
            value = "Sorting criteria in the format: property,asc|desc. " +
                "Default sort order is ascending. Multiple sort criteria is supported.",
            dataTypeClass = String.class,
            paramType = "query",
            allowMultiple = true
        )
    }
)
public @interface ApiPageable {

}