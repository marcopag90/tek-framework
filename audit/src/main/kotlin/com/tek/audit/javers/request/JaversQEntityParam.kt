package com.tek.audit.javers.request

import com.tek.audit.javers.CrudAction
import org.javers.repository.jql.QueryBuilder
import java.time.LocalDate

/**
 * Javers _Entity_ parameters for [QueryBuilder]
 */
data class JaversQEntityParam(
    var author: String? = null,
    var from: LocalDate? = null,
    var to: LocalDate? = null,
    var crudAction: CrudAction? = null
)