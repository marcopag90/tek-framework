package com.tek.security.common.repository

import com.querydsl.core.types.Predicate
import com.tek.security.common.model.RoleName
import com.tek.security.common.model.TEK_ROLE_FULL
import com.tek.security.common.model.TekRole
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@JaversSpringDataAuditable
interface TekRoleRepository : JpaRepository<TekRole, Long>, QuerydslPredicateExecutor<TekRole> {

//    @EntityGraph(value = TEK_ROLE_FULL, type = EntityGraph.EntityGraphType.LOAD)
    fun findByName(name: RoleName): Optional<TekRole>

//    @EntityGraph(value = TEK_ROLE_FULL, type = EntityGraph.EntityGraphType.LOAD)
    override fun findOne(predicate: Predicate): Optional<TekRole>
}