package com.tek.core.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

/**
 * JBot Repository for common _CRUD_ operations
 */
@NoRepositoryBean
interface TekEntityRepository<Entity, ID> : JpaRepository<Entity, ID>,
    QuerydslPredicateExecutor<Entity> {

    override fun findById(id: ID): Optional<Entity>

}