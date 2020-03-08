package com.tek.security.common.service

import com.querydsl.core.types.Predicate
import com.tek.security.common.model.TekPrivilege
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TekPrivilegeService {

    fun list(pageable: Pageable, predicate: Predicate?): Page<TekPrivilege>

    fun readOne(id: Long): TekPrivilege
}