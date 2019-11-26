package it.jbot.core.configuration

import it.jbot.core.AbstractJBotAdapter
import it.jbot.core.component.JBotServiceHelper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.reflect.full.findAnnotation

/**
 * Annotation to mark an Entity for a [AbstractJBotAdapter] implementation
 *
 * Every Entity _CAN_ have only one [AbstractJBotAdapter] extension, defined by the [name] property
 */
@Target(AnnotationTarget.CLASS)
annotation class JBotAdapter(val name: String)

/**
 * Configuration to create an hashMap<K,V> where:
 *
 * 1) _K_ is the [javax.persistence.Entity] class
 * 2) _V_ is the [AbstractJBotAdapter] associated with it
 */
@Configuration
class JBotAdapterConfiguration(
    private val serviceHelper: JBotServiceHelper
) {

    @Bean
    fun adapterMap(): HashMap<Class<*>, AbstractJBotAdapter<*>> {

        val map = hashMapOf<Class<*>, AbstractJBotAdapter<*>>()
        val entities = serviceHelper.entityManager.metamodel.entities

        entities.forEach { entity ->
            entity.javaType.kotlin.findAnnotation<JBotAdapter>()?.let {
                map[entity.javaType] = serviceHelper.getAdapter(it.name)
            }
        }
        return map
    }

    fun getAdapter(entityName: String): AbstractJBotAdapter<*> {
        val entityClass = serviceHelper.getEntityClass(entityName)
        val adapter = adapterMap()[entityClass]
        check(adapter != null)
        return adapter
    }
}