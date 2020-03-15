package com.tek.security.common.service

import com.querydsl.core.types.Predicate
import com.tek.security.common.model.TekRole
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TekRoleService {

    fun list(pageable: Pageable, predicate: Predicate?): Page<TekRole>

    fun readOne(name: String): TekRole
}