package com.tek.audit.service.impl

import com.tek.audit.i18n.AuditMessageSource
import com.tek.audit.i18n.AuditMessageSource.Companion.javersEntityRemoved
import com.tek.audit.i18n.AuditMessageSource.Companion.javersPropertyAdded
import com.tek.audit.i18n.AuditMessageSource.Companion.javersPropertyRemoved
import com.tek.audit.i18n.AuditMessageSource.Companion.javersPropertyValueChanged
import com.tek.audit.javers.*
import com.tek.audit.service.JaversQService
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
import org.javers.repository.jql.QueryBuilder
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Suppress("unused")
@Service
class JaversQServiceImpl(
    private val javers: Javers,
    private val manager: TekEntityManager,
    private val auditMessageSource: AuditMessageSource
) : JaversQService {

    private val log by LoggerDelegate()

    override fun getAuditableEntities(): List<String> {

        val entities = manager.entities
        val javersEntities = mutableListOf<String>()
        entities.forEach { entity ->
            val repoClass = manager.getTekRepository(entity.name)
            AnnotationUtils.findAnnotation(repoClass, JaversSpringDataAuditable::class.java)?.let {
                javersEntities.add(entity.name)
            }
        }
        return javersEntities
    }

    override fun queryChangesByEntity(
        entityName: String,
        page: JaversQPage,
        params: JaversQEntityParam
    ): List<JaversEntityListChanges> {

        val qb: QueryBuilder = QueryBuilder.byClass(manager.getEntityClass(entityName))
        qb.withPagination(page)
        qb.withCommonParams(params)
        params.crudAction?.let {
            when (it) {
                CrudAction.INSERT -> qb.setInsertedSnapshot()
                CrudAction.UPDATE -> qb.setUpdatedSnapshot()
                CrudAction.DELETE -> qb.setDeletedSnapshot()
            }
        } ?: qb.withNewObjectChanges()

        val result = javers.findChanges(qb.build()).groupByCommit()
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

        val clazz = manager.getEntityClass(entityName)
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

    private fun QueryBuilder.withCommonParams(params: JaversQEntityParam?): QueryBuilder {
        if (params != null) {
            params.author?.let { it -> if (it.isNotBlank()) this.byAuthor(it) }
            params.from?.let { it -> this.from(it) }
            params.to?.let { it -> this.toMax(it) }
        }
        return this
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
}