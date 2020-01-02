package com.tek.audit.service.impl

import com.tek.audit.javers.JaversCommitChanges
import com.tek.audit.javers.JaversQParam
import com.tek.audit.service.JaversService
import org.javers.core.Javers
import org.springframework.stereotype.Service
import kotlin.reflect.KClass

@Suppress("UNUSED")
@Service(JaversService.base)
//TODO Javers base implementation without security
class JaversBaseServiceImpl(
    private val javers: Javers
) : JaversService {

    override fun listByCommit(
        id: String,
        kClass: KClass<*>,
        skip: Int,
        limit: Int,
        filters: JaversQParam
    ): List<JaversCommitChanges> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}