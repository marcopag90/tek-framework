package com.tek.audit.javers.request

import com.tek.audit.javers.JaversAction
import org.javers.repository.jql.QueryBuilder
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Javers parameters for [QueryBuilder]
 */
data class JaversQParam(
    var commitId: BigDecimal? = null,
    var author: String? = null,
    var action: JaversAction? = null,
    var from: LocalDate? = null,
    var to: LocalDate? = null
)