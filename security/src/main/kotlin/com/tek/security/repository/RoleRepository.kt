package com.tek.security.repository

import com.tek.core.repository.TekRepository
import com.tek.security.model.auth.PRIVILEGES
import com.tek.security.model.auth.Role
import com.tek.security.model.enums.RoleName
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.stereotype.Repository

@Repository
@JaversSpringDataAuditable
interface RoleRepository : TekRepository<Role, Long> {

    @EntityGraph(value = PRIVILEGES)
    fun findByName(name: RoleName): Role?
}