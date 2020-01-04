package com.tek.security.repository

import com.tek.core.repository.TekRepository
import com.tek.security.model.Role
import com.tek.security.model.enums.RoleName
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.stereotype.Repository

@Repository
@JaversSpringDataAuditable
interface RoleRepository : TekRepository<Role, Long> {

    fun findByName(name: RoleName): Role?
}