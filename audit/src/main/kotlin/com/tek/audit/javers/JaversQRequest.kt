package com.tek.audit.javers

import org.javers.repository.jql.QueryBuilder
import java.time.LocalDate

/**
 * Crud operation performed over a given Entity
 */
enum class CrudAction {
    INSERT,
    UPDATE,
    DELETE
}

/**
 * Javers _pageable_ parameters
 */
data class JaversQPage(
    var skip: Int?,
    var limit: Int?
)

/**
 * Javers _Entity_ parameters for [QueryBuilder]
 */
data class JaversQEntityParam(
    var author: String?,
    var from: LocalDate?,
    var to: LocalDate?,
    var crudAction: CrudAction?
)





