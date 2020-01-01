package com.tek.security.service

import com.querydsl.core.types.Predicate
import com.tek.security.model.Privilege
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PrivilegeService {

    fun list(pageable: Pageable, predicate: Predicate?): Page<Privilege>

    fun readOne(id: Long): Privilege
}