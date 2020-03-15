package com.tek.security.common.repository

import com.querydsl.core.types.Predicate
import com.tek.core.repository.TekEntityRepository
import com.tek.security.common.model.TEK_PROFILE_FULL
import com.tek.security.common.model.TekProfile
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@JaversSpringDataAuditable
interface TekProfileRepository : TekEntityRepository<TekProfile, Long> {

    @EntityGraph(value = TEK_PROFILE_FULL, type = EntityGraph.EntityGraphType.LOAD)
    override fun findOne(predicate: Predicate): Optional<TekProfile>

    @EntityGraph(value = TEK_PROFILE_FULL, type = EntityGraph.EntityGraphType.LOAD)
    fun findByName(name: String): Optional<TekProfile>
}