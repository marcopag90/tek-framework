package com.tek.audit.service

import com.tek.audit.javers.request.JaversQEntityParam
import com.tek.audit.javers.response.JaversCommitChanges
import com.tek.audit.javers.request.JaversQParam

/**
 * Interface to implement business logic for Javers Api Query Language
 */
interface JaversService {

    fun queryByEntity(
        entityName: String,
        skip: Int?,
        limit: Int?,
        params: JaversQEntityParam
    ): List<JaversCommitChanges>

    /**
     * Qualifier for [JaversService] implementations
     */
    companion object Type {
        const val base = "base"
        const val security = "security"
    }
}