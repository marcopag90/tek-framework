package it.jbot.core.dal

import it.jbot.core.DalService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.reflect.full.findAnnotation

/**
 * Annotation to mark an Entity for a [DalService] implementation
 *
 * Every Entity _CAN_ have only one [DalService] extension, defined by the [name] property
 */
@Target(AnnotationTarget.CLASS)
annotation class DalService(val name: String)

/**
 * Configuration to create an hashMap<K,V> where:
 *
 * 1) _K_ is the [javax.persistence.Entity] class
 * 2) _V_ is the [DalService] associated with it
 */
@Configuration
class DalServiceConfiguration(
    private val serviceUtil: DalServiceUtil
) {

    @Bean
    fun serviceMap(): HashMap<Class<*>, DalService<*>> {

        val map = hashMapOf<Class<*>, DalService<*>>()
        val entities = serviceUtil.entityManager.metamodel.entities

        entities.forEach { entity ->
            entity.javaType.kotlin.findAnnotation<it.jbot.core.dal.DalService>()?.let {
                map[entity.javaType] = serviceUtil.getAdapter(it.name)
            }
        }
        return map
    }

    fun getDalService(entityName: String): DalService<*> {
        val entityClass = serviceUtil.getEntityClass(entityName)
        val adapter = serviceMap()[entityClass]
        check(adapter != null)
        return adapter
    }
}