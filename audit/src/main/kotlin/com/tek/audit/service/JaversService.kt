package com.tek.audit.service

import com.tek.audit.javers.request.JaversQEntityParam
import com.tek.audit.javers.response.JaversEntityChanges
import com.tek.audit.javers.response.JaversEntityListChanges
import java.math.BigDecimal

/**
 * Interface to implement business logic for Javers Api Query Language
 */
interface JaversService {

    fun getAuditableEntities(): List<String>

    fun queryChangesByEntity(
        entityName: String,
        skip: Int?,
        limit: Int?,
        params: JaversQEntityParam
    ): List<JaversEntityListChanges>

    fun queryChangesByCommit(entityName: String, id: BigDecimal): List<JaversEntityChanges>
}