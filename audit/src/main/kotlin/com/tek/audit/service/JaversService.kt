package com.tek.audit.service

import com.tek.audit.javers.JaversCommitChanges
import com.tek.audit.javers.JaversQParam
import kotlin.reflect.KClass

/**
 * Interface to implement business logic for Javers Api Query Language
 */
interface JaversService {

    fun listByCommit(
        id: String,
        kClass: KClass<*>,
        skip: Int,
        limit: Int,
        filters: JaversQParam
    ): List<JaversCommitChanges>

    /**
     * Qualifier for [JaversService] implementations
     */
    companion object Type {
        const val base = "base"
        const val security = "security"
    }
}