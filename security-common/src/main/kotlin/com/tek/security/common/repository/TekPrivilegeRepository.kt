package com.tek.security.common.repository

import com.tek.core.repository.TekRepository
import com.tek.security.common.model.TekPrivilege
import com.tek.security.common.model.enums.PrivilegeName
import org.javers.spring.annotation.JaversSpringDataAuditable
import org.springframework.stereotype.Repository

@Repository
@JaversSpringDataAuditable
interface TekPrivilegeRepository : TekRepository<TekPrivilege, Long> {

    fun findByName(name: PrivilegeName): TekPrivilege?
}