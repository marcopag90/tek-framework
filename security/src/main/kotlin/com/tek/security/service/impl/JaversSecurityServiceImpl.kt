package com.tek.security.service.impl

import com.tek.audit.javers.*
import com.tek.audit.javers.request.JaversQEntityParam
import com.tek.audit.javers.request.JaversQParam
import com.tek.audit.javers.response.JaversCommitChanges
import com.tek.audit.service.JaversService
import com.tek.core.repository.TekEntityManager
import com.tek.core.util.LoggerDelegate
import org.javers.core.Javers
import org.javers.core.metamodel.`object`.SnapshotType
import org.javers.repository.jql.JqlQuery
import org.javers.repository.jql.QueryBuilder
import org.springframework.stereotype.Service

@Suppress("UNUSED")
@Service(JaversService.security)
class JaversSecurityServiceImpl(
    private val javers: Javers,
    private val tekEntityManager: TekEntityManager
) : JaversService {

    private val log by LoggerDelegate()

    override fun queryByEntity(
        entityName: String,
        skip: Int?,
        limit: Int?,
        params: JaversQEntityParam
    ): List<JaversCommitChanges> {

        val cr = System.lineSeparator()
        val inputString = StringBuilder("Performing [queryByEntity] with the following parameters: $cr")
        inputString.append("entityName: $entityName $cr")
        inputString.append("skip: $skip $cr")
        inputString.append("limit: $limit $cr")
        inputString.append("params: $params $cr")
        log.debug(inputString.toString())

        val clazz = tekEntityManager.getEntity(entityName)
        val qb: QueryBuilder = QueryBuilder.byClass(clazz)
        val jqlQuery = qb.buildEntityFilters(skip, limit, params)

        val result = javers.findChanges(jqlQuery).groupByCommit()

        val changes = mutableListOf<JaversCommitChanges>()
        for (res in result)
            changes.add(
                JaversCommitChanges(metadata = res.commit, changes = res.get())
            )
        return changes //TODO refactor to give better response
    }

    fun QueryBuilder.buildEntityFilters(
        skip: Int? = 0,
        limit: Int? = 20,
        qParams: JaversQEntityParam? = null
    ): JqlQuery {

        skip?.let { this.skip(it) }
        limit?.let { this.limit(it) }
        qParams?.let {
            it.author?.let { author -> if (author.isNotBlank()) this.byAuthor(author) }
            it.from?.let { dateFrom -> this.from(dateFrom) }
            it.to?.let { dateTo -> this.toMax(dateTo) }

            it.action?.let { action ->
                when (action) {
                    JaversAction.INSERT -> setInsertedSnapshot()
                    JaversAction.UPDATE -> setUpdatedSnapshot()
                    JaversAction.DELETE -> setDeletedSnapshot()
                }
            } ?: this.withNewObjectChanges(true)
        }
        return this.build()
    }
}