package com.tek.audit.javers

import org.javers.repository.jql.QueryBuilder
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Javers parameters for [QueryBuilder]
 */
data class JaversQParam(

    var commitId: BigDecimal? = null,
    var author: String? = null,
    var date: LocalDate? = null,
    var action: JaversAction? = null,
    var from: LocalDate? = null,
    var to: LocalDate? = null
)