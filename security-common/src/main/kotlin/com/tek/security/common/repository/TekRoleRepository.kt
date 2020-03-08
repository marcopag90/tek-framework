package com.tek.security.common.repository

import com.tek.core.repository.TekRepository
import com.tek.security.common.model.TEK_ROLE_FULL
import com.tek.security.common.model.TekRole
import com.tek.security.common.model.enums.RoleName
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.stereotype.Repository

@Repository
@JaversSpringDataAuditable
interface TekRoleRepository : TekRepository<TekRole, Long> {

    @EntityGraph(value = TEK_ROLE_FULL)
    fun findByName(name: RoleName): TekRole?
}