package it.jbot.core.component

import org.springframework.context.ApplicationContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.support.Repositories
import org.springframework.stereotype.Component
import javax.persistence.EntityManager
import javax.persistence.metamodel.EntityType

@Component
class JBotCrudHelper(
    val appContext: ApplicationContext,
    val repositories: Repositories = Repositories(appContext),
    val entityManager: EntityManager
) {

    fun getJpaRepository(entityClass: Class<*>): JpaRepository<*, *> {

        val repo = repositories.getRepositoryFor(entityClass).get()
        check(repo is JpaRepository<*, *>) {
            "Repository of entity $entityClass must extends ${JpaRepository::class.java}"
        }
        return repo
    }

    fun getJpaRepository(entityName: String): JpaRepository<*, *> {

        val entityClass = getEntityClass(entityName)
        val repo = repositories.getRepositoryFor(entityClass).get()
        check(repo is JpaRepository<*, *>) {
            "Repository of entity $entityClass must extends ${JpaRepository::class.java}"
        }
        return repo
    }

    fun getEntityClass(entityName: String): Class<*> {

        val entityClass: EntityType<*>? = entityManager.metamodel.entities.find { it.name == entityName.capitalize() }
        check(entityClass != null) {
            "Unable to find a suitable class for given entity: $entityName" //verificare
        }
        return entityClass.javaType
    }
}