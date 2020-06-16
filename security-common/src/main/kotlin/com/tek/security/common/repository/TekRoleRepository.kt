package com.tek.security.common.repository

import com.tek.core.repository.TekRepository
import com.tek.security.common.model.TekRole
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@JaversSpringDataAuditable
interface TekRoleRepository : TekRepository<TekRole, Long> {

    fun findByName(name: String): Optional<TekRole>
    fun existsByName(name: String): Boolean
}