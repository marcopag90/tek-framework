package com.tek.audit.service.impl

import com.tek.audit.i18n.AuditMessageSource
import com.tek.audit.i18n.AuditMessageSource.Companion.javersEntityRemoved
import com.tek.audit.i18n.AuditMessageSource.Companion.javersPropertyAdded
import com.tek.audit.i18n.AuditMessageSource.Companion.javersPropertyRemoved
import com.tek.audit.i18n.AuditMessageSource.Companion.javersPropertyValueChanged
import com.tek.audit.javers.*
import com.tek.audit.javers.request.JaversQEntityParam
import com.tek.audit.javers.response.EntityValues
import com.tek.audit.javers.response.JaversEntityChanges
import com.tek.audit.javers.response.JaversEntityListChanges
import com.tek.audit.service.JaversService
import com.tek.core.repository.TekEntityManager
import com.tek.core.util.LoggerDelegate
import com.tek.core.util.doNothing
import org.javers.core.Javers
import org.javers.core.diff.changetype.NewObject
import org.javers.core.diff.changetype.ObjectRemoved
import org.javers.core.diff.changetype.PropertyChangeType
import org.javers.core.diff.changetype.ValueChange
import org.javers.core.diff.changetype.container.SetChange
import org.javers.core.metamodel.`object`.InstanceId
import org.javers.repository.jql.JqlQuery
import org.javers.repository.jql.QueryBuilder
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.context.ApplicationContext
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.support.Repositories
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Suppress("UNUSED")
@Service
class JaversServiceImpl(
    private val javers: Javers,
    private val tekEntityManager: TekEntityManager,
    private val appContext: ApplicationContext,
    private val repositories: Repositories = Repositories(appContext),
    private val auditMessageSource: AuditMessageSource
) : JaversService {

    private val log by LoggerDelegate()

    override fun getAuditableEntities(): List<String> {

        val entities = tekEntityManager.getEntities()
        val javersEntities = mutableListOf<String>()
        entities.forEach { type ->
            val clazz = type.javaType
            val repoClass = (repositories.getRepositoryFor(clazz).get() as JpaRepository<*, *>)::class.java
            AnnotationUtils.findAnnotation(repoClass, JaversSpringDataAuditable::class.java)?.let {
                javersEntities.add(clazz.simpleName)
            }
        }
        return javersEntities
    }

    override fun queryChangesByEntity(
        entityName: String,
        skip: Int?,
        limit: Int?,
        params: JaversQEntityParam
    ): List<JaversEntityListChanges> {

        val cr = System.lineSeparator()
        val inputString = StringBuilder("Performing [queryChangesByEntity] with the following parameters: $cr")
        inputString.append("entityName: $entityName $cr")
        inputString.append("skip: $skip $cr")
        inputString.append("limit: $limit $cr")
        inputString.append("params: $params $cr")
        log.debug(inputString.toString())

        getAuditableEntities()

        val clazz = tekEntityManager.getEntity(entityName)
        val qb: QueryBuilder = QueryBuilder.byClass(clazz)
        val jqlQuery = qb.buildEntityFilters(skip, limit, params)

        val result = javers.findChanges(jqlQuery).groupByCommit()

        val changes = mutableListOf<JaversEntityListChanges>()
        for (res in result) {
            changes.add(
                JaversEntityListChanges(
                    commitId = res.commit.id.valueAsNumber(),
                    commitDate = res.commit.commitDate,
                    author = res.commit.author,
                    properties = res.commit.properties
                )
            )
        }
        return changes
    }

    @Suppress("UNCHECKED_CAST")
    override fun queryChangesByCommit(entityName: String, id: BigDecimal): List<JaversEntityChanges> {
        log.debug("Performing [queryChangesByCommit] on entity: $entityName with the following commit id: $id")

        val clazz = tekEntityManager.getEntity(entityName)
        val jqlQuery =
            QueryBuilder.byClass(clazz).withCommitId(id)
                .withNewObjectChanges().build()

        val result = javers.findChanges(jqlQuery).groupByCommit()

        val changes = mutableListOf<JaversEntityChanges>()
        result.takeIf { result.size > 0 }?.let {
            val obj = it[0].get()
            try {
                (obj as List<*>).forEach { change ->
                    when (change) {
                        is SetChange -> changes.add(
                            JaversEntityChanges(
                                changeType = resolveChangeType(change.changeType),
                                propertyName = change.propertyName,
                                propertyPath = change.propertyNameWithPath,
                                entityName = change.affectedGlobalId.typeName,
                                entityId = (change.affectedGlobalId as InstanceId).cdoId,
                                addedValues = createValueList(change.addedValues as ArrayList<InstanceId>),
                                removedValues = createValueList(change.removedValues as ArrayList<InstanceId>)
                            )
                        )
                        is ValueChange -> changes.add(
                            JaversEntityChanges(
                                changeType = resolveChangeType(change.changeType),
                                propertyName = change.propertyName,
                                propertyPath = change.propertyNameWithPath,
                                entityName = change.affectedGlobalId.typeName,
                                entityId = (change.affectedGlobalId as InstanceId).cdoId,
                                fromValue = change.left,
                                toValue = change.right
                            )
                        )
                        is NewObject -> doNothing()
                        is ObjectRemoved -> changes.add(
                            JaversEntityChanges(
                                changeType = auditMessageSource.getAuditSource().getMessage(
                                    javersEntityRemoved, null, LocaleContextHolder.getLocale()
                                ),
                                entityName = change.affectedGlobalId.typeName,
                                entityId = (change.affectedGlobalId as InstanceId).cdoId
                            )
                        )
                        else -> throw NotImplementedError("Missing change type implementation")
                    }
                }
            } catch (ex: Exception) {
                throw NotImplementedError("Missing type implementation")
            }
        }
        return changes
    }

    private fun resolveChangeType(changeType: PropertyChangeType): String? {
        return when (changeType) {
            PropertyChangeType.PROPERTY_ADDED -> auditMessageSource.getAuditSource().getMessage(
                javersPropertyAdded, null, LocaleContextHolder.getLocale()
            )
            PropertyChangeType.PROPERTY_REMOVED -> auditMessageSource.getAuditSource().getMessage(
                javersPropertyRemoved, null, LocaleContextHolder.getLocale()
            )
            PropertyChangeType.PROPERTY_VALUE_CHANGED -> auditMessageSource.getAuditSource().getMessage(
                javersPropertyValueChanged, null, LocaleContextHolder.getLocale()
            )
            else -> throw NotImplementedError()
        }
    }

    private fun createValueList(list: ArrayList<InstanceId>?): List<EntityValues>? {
        if (list.isNullOrEmpty()) return null
        return mutableListOf<EntityValues>().apply {
            list.forEach { instanceId ->
                this.add(
                    EntityValues(
                        typeName = instanceId.typeName,
                        value = instanceId.cdoId
                    )
                )
            }
        }
    }

    private fun QueryBuilder.buildEntityFilters(
        skip: Int? = 0,
        limit: Int? = 20,
        qParams: JaversQEntityParam? = null
    ): JqlQuery {

        // Pagination
        skip?.let { this.skip(it) }
        limit?.let { this.limit(it) }

        // Query Parameters
        qParams?.let {

            it.author?.let { author -> if (author.isNotBlank()) this.byAuthor(author) }
            it.from?.let { dateFrom -> this.from(dateFrom) }
            it.to?.let { dateTo -> this.toMax(dateTo) }

            it.crudAction?.let { crudAction ->
                when (crudAction) {
                    CrudAction.INSERT -> setInsertedSnapshot()
                    CrudAction.UPDATE -> setUpdatedSnapshot()
                    CrudAction.DELETE -> setDeletedSnapshot()
                }
            } ?: this.withNewObjectChanges(true)
        }
        return this.build()
    }
}