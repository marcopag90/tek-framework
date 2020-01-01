package com.tek.security.service

import com.querydsl.core.types.Predicate
import com.tek.security.model.Role
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface RoleService {

    fun list(pageable: Pageable, predicate: Predicate?): Page<Role>

    fun readOne(id: Long): Role
}