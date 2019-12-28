package com.tek.security.service

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.security.model.Role
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

interface RoleService {

    fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<TekPageResponse<Role>>

    fun read(name: String): ResponseEntity<TekResponseEntity<Role>>
}