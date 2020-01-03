package com.tek.audit.javers.response

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