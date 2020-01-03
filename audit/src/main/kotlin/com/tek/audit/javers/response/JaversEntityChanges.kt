package com.tek.audit.javers.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class JaversEntityChanges(
    val changeType: String? = null,
    val propertyName: String? = null,
    val propertyPath: String? = null,
    val entityName: String? = null,
    val entityId: Any? = null,
    val fromValue: Any? = null,
    val toValue: Any? = null,
    val removedValues: List<EntityValues>? = null,
    val addedValues: List<EntityValues>? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EntityValues(
    val typeName: String? = null,
    val value: Any? = null
)
