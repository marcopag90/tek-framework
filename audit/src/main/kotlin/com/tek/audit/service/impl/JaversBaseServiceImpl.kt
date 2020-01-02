package com.tek.audit.service.impl

import com.tek.audit.javers.request.JaversQEntityParam
import com.tek.audit.javers.response.JaversCommitChanges
import com.tek.audit.javers.request.JaversQParam
import com.tek.audit.service.JaversService
import com.tek.core.repository.TekEntityManager
import org.javers.core.Javers
import org.springframework.stereotype.Service
import kotlin.reflect.KClass

@Suppress("UNUSED")
@Service(JaversService.base)
//TODO Javers base implementation without security
class JaversBaseServiceImpl(
    private val javers: Javers,
    private val tekEntityManager: TekEntityManager
) : JaversService {

    override fun queryByEntity(
        entityName: String,
        skip: Int?,
        limit: Int?,
        params: JaversQEntityParam
    ): List<JaversCommitChanges> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}