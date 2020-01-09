package com.tek.audit.javers

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class JaversEntityListChanges(
    val commitId: BigDecimal? = null,
    val commitDate: LocalDateTime? = null,
    val author: String? = null,
    val properties: Map<String, String?>? = null
)

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