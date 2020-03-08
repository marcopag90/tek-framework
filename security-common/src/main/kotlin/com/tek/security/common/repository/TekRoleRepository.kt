package com.tek.security.common.repository

import com.querydsl.core.types.Predicate
import com.tek.core.repository.TekRepository
import com.tek.security.common.model.TEK_ROLE_FULL
import com.tek.security.common.model.TekRole
import com.tek.security.common.model.enums.RoleName
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@JaversSpringDataAuditable
interface TekRoleRepository : TekRepository<TekRole, Long> {

    @EntityGraph(value = TEK_ROLE_FULL, type = EntityGraph.EntityGraphType.LOAD)
    fun findByName(name: RoleName): TekRole?

    @EntityGraph(value = TEK_ROLE_FULL, type = EntityGraph.EntityGraphType.LOAD)
    override fun findById(id: Long): Optional<TekRole>

    @EntityGraph(value = TEK_ROLE_FULL, type = EntityGraph.EntityGraphType.LOAD)
    override fun findOne(predicate: Predicate): Optional<TekRole>
}