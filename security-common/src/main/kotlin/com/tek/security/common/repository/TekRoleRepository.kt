package com.tek.security.common.repository

import com.tek.security.common.model.RoleName
import com.tek.security.common.model.TekRole
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@JaversSpringDataAuditable
interface TekRoleRepository : JpaRepository<TekRole, Long>, QuerydslPredicateExecutor<TekRole> {

    fun findByName(name: RoleName): Optional<TekRole>
}