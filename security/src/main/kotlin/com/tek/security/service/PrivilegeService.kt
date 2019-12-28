package com.tek.security.service

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.security.model.Privilege
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

interface PrivilegeService {

    fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<TekPageResponse<Privilege>>

    fun read(name: String): ResponseEntity<TekResponseEntity<Privilege>>
}