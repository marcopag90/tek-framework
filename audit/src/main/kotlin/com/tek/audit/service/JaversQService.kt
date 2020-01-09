package com.tek.audit.service

import com.tek.audit.javers.JaversEntityChanges
import com.tek.audit.javers.JaversEntityListChanges
import com.tek.audit.javers.JaversQEntityParam
import com.tek.audit.javers.JaversQPage
import java.math.BigDecimal

/**
 * Interface to implement business logic for Javers Api Query Language
 */
interface JaversQService {

    fun getAuditableEntities(): List<String>

    fun queryChangesByEntity(
        entityName: String,
        page: JaversQPage,
        params: JaversQEntityParam
    ): List<JaversEntityListChanges>

    fun queryChangesByCommit(entityName: String, id: BigDecimal): List<JaversEntityChanges>
}