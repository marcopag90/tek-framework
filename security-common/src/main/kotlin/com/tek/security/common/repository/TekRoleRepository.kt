package com.tek.security.common.repository

import com.tek.core.repository.TekRepository
import com.tek.security.common.model.RoleName
import com.tek.security.common.model.TekRole
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.stereotype.Repository

@Repository
@JaversSpringDataAuditable
interface TekRoleRepository : TekRepository<TekRole, Long> {

    fun findByName(name: RoleName): TekRole?
}