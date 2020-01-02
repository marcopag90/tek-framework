package com.tek.core.repository

import com.tek.core.util.LoggerDelegate
import com.tek.core.util.allAnnotations
import com.tek.core.util.findInstanceOrNull
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
    private val entityManager: EntityManager
) {

    private val log by LoggerDelegate()

    fun getEntity(entityName: String): Class<*> {
        log.debug("Trying to load entity: $entityName")

        val entityClass: EntityType<*>? = entityManager.metamodel.entities.find { it.name == entityName.capitalize() }
        check(entityClass != null) {
            "Unable to find a suitable class for given entity: $entityName" //verificare
        }
        log.debug("Entity loaded: ${entityClass.javaType}")
        return entityClass.javaType
    }

    fun getEntityIdType(clazz: Class<*>, id: String): Any {

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
}