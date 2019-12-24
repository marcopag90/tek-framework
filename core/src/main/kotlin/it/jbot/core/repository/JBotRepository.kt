package it.jbot.core.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.NoRepositoryBean

/**
 * JBot Repository for common _CRUD_ operations
 */
@NoRepositoryBean
interface JBotRepository<E, ID> : JpaRepository<E, ID>, QuerydslPredicateExecutor<E> {
}