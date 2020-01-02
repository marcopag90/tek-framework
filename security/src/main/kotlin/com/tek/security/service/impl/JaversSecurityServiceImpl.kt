package com.tek.security.service.impl

import com.tek.audit.javers.*
import com.tek.audit.service.JaversService
import com.tek.core.util.allAnnotations
import com.tek.core.util.findInstanceOrNull
import org.javers.core.Javers
import org.javers.core.metamodel.`object`.SnapshotType
import org.javers.repository.jql.JqlQuery
import org.javers.repository.jql.QueryBuilder
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.persistence.Id
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmErasure

@Suppress("UNUSED")
@Service(JaversService.security)
class JaversSecurityServiceImpl(
    private val javers: Javers
) : JaversService {

    override fun listByCommit(
        id: String,
        kClass: KClass<*>,
        skip: Int,
        limit: Int,
        filters: JaversQParam
    ): List<JaversCommitChanges> {

        val idType: Any = getIdType(kClass, id)
        val qb: QueryBuilder = QueryBuilder.byInstanceId(idType, kClass.java)
        val jqlQuery = qb.buildFilters(skip, limit, filters)

        val result = javers.findChanges(jqlQuery).groupByCommit()

        val changes = mutableListOf<JaversCommitChanges>()
        result.forEach { commitChanges ->
            changes.apply {
                JaversCommitChanges(
                    metadata = commitChanges.commit,
                    changes = commitChanges.get()
                ).let(::add)
            }
        }
        return changes
    }

    private fun getIdType(kClass: KClass<*>, id: String): Any {

        return when (kClass.memberProperties.single {
            it.allAnnotations.findInstanceOrNull<Id>() != null
        }.returnType.jvmErasure) {
            String::class -> id
            Int::class -> id.toInt()
            Long::class -> id.toLong()
            BigDecimal::class -> id.toBigDecimal()
            else -> TODO("Missing type implementation")
        }
    }

    fun QueryBuilder.buildFilters(
        skip: Int? = 0,
        limit: Int? = 20,
        qParams: JaversQParam? = null
    ): JqlQuery {

        skip?.let { this.skip(it) }
        limit?.let { this.limit(it) }
        qParams?.let {
            it.commitId?.let { commit -> this.withCommitId(commit) }
            it.author?.let { author -> if (author.isNotBlank()) this.byAuthor(author) }
            it.from?.let { date -> this.from(date) }
            it.to?.let { date -> this.toMax(date) }
            it.action?.let { action ->
                when (action) {
                    JaversAction.INSERT -> setInsertedSnapshot()
                    JaversAction.UPDATE -> setUpdatedSnapshot()
                    JaversAction.DELETE -> this.withSnapshotType(SnapshotType.TERMINAL)
                }
            }
        }
        return this.build()
    }
}