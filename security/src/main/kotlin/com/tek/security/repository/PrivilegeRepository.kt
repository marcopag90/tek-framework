package com.tek.security.repository

import com.tek.core.repository.TekRepository
import com.tek.security.model.Privilege
import com.tek.security.model.enums.PrivilegeName
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.stereotype.Repository

@Repository
@JaversSpringDataAuditable
interface PrivilegeRepository : TekRepository<Privilege, Long> {

    fun findByName(name: PrivilegeName): Privilege?
}