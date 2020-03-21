package com.tek.security.common.repository

import com.querydsl.core.types.Predicate
import com.tek.security.common.model.TEK_USER_FULL
import com.tek.security.common.model.TekUser
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional
@JaversSpringDataAuditable
interface TekUserRepository : JpaRepository<TekUser, Long>, QuerydslPredicateExecutor<TekUser> {

    @EntityGraph(value = TEK_USER_FULL, type = EntityGraph.EntityGraphType.LOAD)
    fun findByUsername(username: String): Optional<TekUser>

    @EntityGraph(value = TEK_USER_FULL, type = EntityGraph.EntityGraphType.LOAD)
    override fun findById(id: Long): Optional<TekUser>

    @EntityGraph(value = TEK_USER_FULL, type = EntityGraph.EntityGraphType.LOAD)
    override fun findOne(predicate: Predicate): Optional<TekUser>

    fun findPreferencesById(id: Long): TekUser?

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean
}