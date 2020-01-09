package com.tek.core.repository

import com.tek.core.util.LoggerDelegate
import com.tek.core.util.allAnnotations
import com.tek.core.util.findInstanceOrNull
import org.springframework.context.ApplicationContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.support.Repositories
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.persistence.EntityManager
import javax.persistence.Id
import javax.persistence.metamodel.EntityType
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmErasure

/**
 * Wrapper utility for [EntityManager]
 */
@Service
class TekEntityManager(
    private val entityManager: EntityManager,
    appContext: ApplicationContext
) {

    private val log by LoggerDelegate()

    val entities: Set<EntityType<*>> = entityManager.metamodel.entities
    val repositories = Repositories(appContext)

    fun getEntityClass(entityName: String): Class<*>? = getEntity(entityName)?.javaType

    fun getEntityKClass(entityName: String): KClass<out Any>? = getEntityClass(entityName)?.kotlin

    fun getIdType(clazz: Class<*>, id: String): Any {

        return when (clazz.kotlin.memberProperties.single {
            it.allAnnotations.findInstanceOrNull<Id>() != null
        }.returnType.jvmErasure) {
            String::class -> id
            Int::class -> id.toInt()
            Long::class -> id.toLong()
            BigDecimal::class -> id.toBigDecimal()
            else -> TODO("Missing type implementation")
        }
    }

    fun getIdName(clazz: Class<*>): Any =
        (clazz.kotlin.memberProperties.single {
            it.allAnnotations.findInstanceOrNull<Id>() != null
        }.name)

    fun getTekRepository(entityName: String): Class<out JpaRepository<*, *>> {
        log.debug("Trying to load ${TekRepository::class.java} for entity: $entityName")
        return (repositories.getRepositoryFor(getEntity(entityName)!!.javaType).get() as JpaRepository<*, *>)::class.java
    }

    private fun getEntity(entityName: String): EntityType<*>? {
        log.debug("Trying to load entity: $entityName")

        val entityClass: EntityType<*>? = entityManager.metamodel.entities.find { it.name == entityName.capitalize() }
        check(entityClass != null) {
            "Unable to find a suitable class for given entity: $entityName" //verificare
        }
        return entityClass
    }
}